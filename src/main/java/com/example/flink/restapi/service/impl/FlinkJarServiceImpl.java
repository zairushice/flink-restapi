package com.example.flink.restapi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.flink.restapi.dao.FlinkJobDAO;
import com.example.flink.restapi.entity.FlinkJobTable;
import com.example.flink.restapi.service.FlinkJobService;
import com.example.flink.restapi.vo.JarVO;
import com.example.flink.restapi.dao.FlinkJarDAO;
import com.example.flink.restapi.dto.*;
import com.example.flink.restapi.entity.FlinkJarTable;
import com.example.flink.restapi.service.FlinkJarService;
import com.example.flink.restapi.util.HttpClientUtil;
import com.example.flink.restapi.vo.JobVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
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

    @Resource
    FlinkJobDAO flinkJobDAO;

    @Resource
    FlinkJobService flinkJobService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public FlinkJarsDTO getJarsData(String host) {
        FlinkJarsDTO flinkJarsDTO = new FlinkJarsDTO();
        HttpClientUtil httpClientUtil = new HttpClientUtil(host);
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
    @Transactional(rollbackFor = Exception.class)
    public FlinkJarUploadDTO uploadJar(JarVO jarVO) {
        FlinkJarUploadDTO flinkJarUploadDTO = new FlinkJarUploadDTO();
        String baseURI = jarVO.getHost();
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
                flinkJarDAO.insert(new FlinkJarTable(jarId, jarVO.getFilename(),
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
    @Transactional(rollbackFor = Exception.class)
    public FlinkJarSubmitDTO runJar(JarVO jarVO) {
        FlinkJarSubmitDTO flinkJarSubmitDTO = new FlinkJarSubmitDTO();
        String baseURI = jarVO.getHost();
        HttpClientUtil httpClientUtil = new HttpClientUtil(baseURI);
        try {
            CloseableHttpResponse response = httpClientUtil.submitJar(jarVO);
            if (response.getStatusLine().getStatusCode() == 200) {
                String str = EntityUtils.toString(response.getEntity());
                String jobId = JSON.parseObject(str).getString("jobid");
                flinkJarSubmitDTO.setJobId(jobId);
                FlinkJobDetailDTO jobDetailDTO = flinkJobService.getFlinkJobDetail(new JobVO(jarVO.getHost(), jobId));
                FlinkJobTable flinkJobTable = new FlinkJobTable(jarVO.getJarId(), jobId, jobDetailDTO.getState(),
                        jobDetailDTO.getStartTime(), jobDetailDTO.getEndTime());
                System.out.println(flinkJobTable);
                flinkJobDAO.insert(flinkJobTable);
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return flinkJarSubmitDTO;
    }
}
