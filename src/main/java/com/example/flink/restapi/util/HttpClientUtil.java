package com.example.flink.restapi.util;

import com.alibaba.fastjson.JSONObject;
import com.example.flink.restapi.vo.JarVO;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {
    private final CloseableHttpClient httpClient;
    private final String baseURI;

    public HttpClientUtil(String baseURI) {
        if (!baseURI.endsWith("/")) {
            this.baseURI = baseURI + "/";
        }else {
            this.baseURI = baseURI;
        }
        httpClient = HttpClientBuilder.create().build();
    }

    public CloseableHttpResponse showJars() throws URISyntaxException, IOException {
        HttpGet httpGet = new HttpGet();
        httpGet.setURI(new URI(baseURI + "v1/jars"));
        return httpClient.execute(httpGet);
    }

    public CloseableHttpResponse uploadJar(JarVO jarVO) throws IOException, URISyntaxException {
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        HttpEntity httpEntity = entityBuilder.addBinaryBody("file",
                new File(jarVO.getFilepath()),
                ContentType.create("application/x-java-archive"), jarVO.getFilename()).build();
        HttpPost httpPost = new HttpPost();
        httpPost.setURI(new URI(baseURI + "v1/jars/upload"));
        httpPost.setEntity(httpEntity);
        return httpClient.execute(httpPost);
    }

    public CloseableHttpResponse showPlan(String jarId, Map<String, String> params) throws URISyntaxException, IOException {
        HttpGet httpGet = new HttpGet();
        URI uri = new URI(baseURI + String.format("v1/jars/%s/plan?", jarId));
        URIBuilder uriBuilder = new URIBuilder(uri);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            uriBuilder.addParameter(entry.getKey(), entry.getValue());
        }
        httpGet.setURI(uriBuilder.build());
        return httpClient.execute(httpGet);
    }

    public CloseableHttpResponse showPlan(String jarId, List<NameValuePair> params) throws URISyntaxException, IOException {
        HttpGet httpGet = new HttpGet();
        URI uri = new URI(baseURI + String.format("v1/jars/%s/plan?", jarId));
        URIBuilder uriBuilder = new URIBuilder(uri);
        uriBuilder.addParameters(params);
        httpGet.setURI(uriBuilder.build());
        return httpClient.execute(httpGet);
    }

    public CloseableHttpResponse submitJar(JarVO jarVO) throws URISyntaxException, IOException {
        HttpPost httpPost = new HttpPost();
        JSONObject params = new JSONObject();
        if (jarVO.getProgramArgs() != null) {
//            final String str = Optional.ofNullable(jarVO.getProgramArgs()).orElse("");
            params.put("programArgs", jarVO.getProgramArgs());
        }
        if (jarVO.getEntryClass() != null) {
            params.put("entry-class", jarVO.getEntryClass());
        }
        if (jarVO.getParallelism() != null) {
            params.put("parallelism", jarVO.getParallelism());
        }
        if (jarVO.getSavePointPath() != null) {
            params.put("savepointPath", jarVO.getSavePointPath());
        }
        // StringEntity可以用于postman上的row格式，UrlEncodedFormEntity的Content-Type是application/x-www-form-urlencoded，
        // MultipartEntityBuilder对应于postman的form-data，可以用于上传文件
        HttpEntity httpEntity = new StringEntity(params.toString(), ContentType.APPLICATION_JSON);
        httpPost.setURI(new URI(baseURI + String.format("v1/jars/%s/run?", jarVO.getJarId())));
        httpPost.setEntity(httpEntity);
        return httpClient.execute(httpPost);
    }

    public CloseableHttpResponse getJobsList() throws URISyntaxException, IOException {
        HttpGet httpGet = new HttpGet();
        httpGet.setURI(new URI(baseURI + "/jobs"));
        return httpClient.execute(httpGet);
    }

    public CloseableHttpResponse getJobDetail(String jobId) throws URISyntaxException, IOException {
        HttpGet httpGet = new HttpGet();
        httpGet.setURI(new URI(baseURI + String.format("/jobs/%s", jobId)));
        return httpClient.execute(httpGet);
    }
}
