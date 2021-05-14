package com.example.flink.restapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.flink.restapi.api.service.FlinkJarService;
import com.example.flink.restapi.api.vo.JarVO;
import com.example.flink.restapi.dao.FlinkJarDAO;
import com.example.flink.restapi.dto.*;
import com.example.flink.restapi.entity.FlinkJar;
import com.example.flink.restapi.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Service
@Slf4j
public class FlinkJarServiceImpl implements FlinkJarService {

    @Resource
    FlinkJarDAO flinkJarDAO;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public FlinkJarsDTO getJarsData(String host) {
        FlinkJarsDTO flinkJarsDTO = new FlinkJarsDTO();
        String baseURI = "http://" + host;
        HttpClientUtil httpClientUtil = new HttpClientUtil(baseURI);
        log.debug(baseURI);
        try {
            CloseableHttpResponse response = httpClientUtil.showJars();
            String str = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSON.parseObject(str);
            flinkJarsDTO.setAddress(jsonObject.getString("address"));
            JSONArray files = jsonObject.getJSONArray("files");
            int fileCount = files.size();
            if (fileCount != 0) {
                ArrayList<FlinkJars> jars = new ArrayList<>();
                for (int i = 0; i < fileCount; i++) {
                    JSONObject jar = files.getJSONObject(i);
                    String id = jar.getString("id");
                    String name = jar.getString("name");
                    Long uploaded = jar.getLong("uploaded");
                    JSONArray entryArray = jar.getJSONArray("entry");
                    FlinkJarEntry ent = new FlinkJarEntry();
                    if (entryArray.size() != 0) {
                        JSONObject entry = entryArray.getJSONObject(0);
                        ent.setName(entry.getString("name"));
                        ent.setDescription(entry.getString("description"));
                    }
                    jars.add(new FlinkJars(id, name, uploaded, ent));
                }
                flinkJarsDTO.setFiles(jars);
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return flinkJarsDTO;
    }

    @Override
    @Transactional
    public FlinkJarUploadDTO uploadJar(JarVO jarVO) {
        FlinkJarUploadDTO flinkJarUploadDTO = new FlinkJarUploadDTO();
        String baseURI = "http://" + jarVO.getHost();
        HttpClientUtil httpClientUtil = new HttpClientUtil(baseURI);
        try {
            CloseableHttpResponse response = httpClientUtil.uploadJar(jarVO);
            String str = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() == 200) {
                JSONObject res = JSON.parseObject(str);
                String fn = res.getString("filename");
                String[] split = fn.split("/");
                String jarId = split[split.length - 1];
                String status = res.getString("status");
                flinkJarUploadDTO.setFilename(fn);
                flinkJarUploadDTO.setStatus(status);
                flinkJarDAO.insert(new FlinkJar(jarId, jarVO.getFilename(),
                        sdf.format(new Date(System.currentTimeMillis())), jarVO.getHost()));
            } else {
                flinkJarUploadDTO.setStatus("fail");
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return flinkJarUploadDTO;
    }

    @Override
    public FlinkJarSubmitDTO runJar(JarVO jarVO) {
        FlinkJarSubmitDTO flinkJarSubmitDTO = new FlinkJarSubmitDTO();
        String baseURI = "http://" + jarVO.getHost();
        HttpClientUtil httpClientUtil = new HttpClientUtil(baseURI);
        try {
            CloseableHttpResponse response = httpClientUtil.submitJar(jarVO);
            if (response.getStatusLine().getStatusCode() == 200) {
                String str = EntityUtils.toString(response.getEntity());
                flinkJarSubmitDTO.setJobId(JSON.parseObject(str).getString("jobid"));
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return flinkJarSubmitDTO;
    }
}
