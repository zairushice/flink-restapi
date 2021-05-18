package com.example.flink.restapi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.flink.restapi.dto.FlinkJob;
import com.example.flink.restapi.dto.FlinkJobsDTO;
import com.example.flink.restapi.service.FlinkJobService;
import com.example.flink.restapi.util.HttpClientUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class FlinkJobServiceImpl implements FlinkJobService {

    String baseURI = "192.168.9.12:8081";
    @Override
    public FlinkJobsDTO getFlinkJobs() {
        HttpClientUtil httpClientUtil = new HttpClientUtil(baseURI);
        ArrayList<FlinkJob> flinkJobs = new ArrayList<>();
        FlinkJobsDTO flinkJobsDTO = new FlinkJobsDTO();
        try {
            CloseableHttpResponse response = httpClientUtil.getJobsList();
            if (response.getStatusLine().getStatusCode() == 200) {
                String s = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = JSON.parseObject(s);
                JSONArray jobs = jsonObject.getJSONArray("jobs");
                for (int i = 0; i < jobs.size(); i++) {
                    JSONObject job = jobs.getJSONObject(i);
                    flinkJobs.add(new FlinkJob(job.getString("id"), job.getString("status")));
                }
                flinkJobsDTO.setJobs(flinkJobs);
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return flinkJobsDTO;
    }
}
