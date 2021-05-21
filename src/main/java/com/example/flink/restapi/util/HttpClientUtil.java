package com.example.flink.restapi.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpClientUtil {
    private final CloseableHttpClient httpClient;
    private final String baseURI;

    public HttpClientUtil(String baseURI) {
        baseURI = "http://" + baseURI;
        if (!baseURI.endsWith("/")) {
            this.baseURI = baseURI + "/";
        }else {
            this.baseURI = baseURI;
        }
        httpClient = HttpClientBuilder.create().build();
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
