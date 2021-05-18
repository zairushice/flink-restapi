package com.example.flink.restapi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.flink.restapi.dao.FlinkJobDAO;
import com.example.flink.restapi.dto.FlinkJob;
import com.example.flink.restapi.dto.FlinkJobDetailDTO;
import com.example.flink.restapi.dto.FlinkJobsDTO;
import com.example.flink.restapi.entity.FlinkJobTable;
import com.example.flink.restapi.service.FlinkJobService;
import com.example.flink.restapi.util.HttpClientUtil;
import com.example.flink.restapi.vo.JobVO;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Service
public class FlinkJobServiceImpl implements FlinkJobService {

    @Resource
    FlinkJobDAO flinkJobDAO;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public FlinkJobsDTO getFlinkJobs(String host) {
        HttpClientUtil httpClientUtil = new HttpClientUtil(host);
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

    @Override
    public FlinkJobDetailDTO getFlinkJobDetail(JobVO jobVO) {
        HttpClientUtil httpClientUtil = new HttpClientUtil(jobVO.getHost());
        FlinkJobDetailDTO flinkJobDetailDTO = new FlinkJobDetailDTO();
        try {
            CloseableHttpResponse response = httpClientUtil.getJobDetail(jobVO.getJobId());
            if (response.getStatusLine().getStatusCode() == 200) {
                String s = EntityUtils.toString(response.getEntity());
                JSONObject entity = JSON.parseObject(s);
                Long startTimestamp = entity.getLong("start-time");
                String startTime = sdf.format(new Date(startTimestamp));
                Long endTimestamp = entity.getLong("end-time");
                if (endTimestamp != -1) {
                    String endTime = sdf.format(new Date(endTimestamp));
                    flinkJobDetailDTO.setEndTime(endTime);
                }
                flinkJobDetailDTO.setJobId(jobVO.getJobId());
                flinkJobDetailDTO.setStartTime(startTime);
                flinkJobDetailDTO.setState(entity.getString("state"));
                flinkJobDAO.updateById(new FlinkJobTable(null, flinkJobDetailDTO.getJobId(),
                        flinkJobDetailDTO.getState(), flinkJobDetailDTO.getStartTime(),
                        flinkJobDetailDTO.getEndTime()));
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return flinkJobDetailDTO;
    }
}
