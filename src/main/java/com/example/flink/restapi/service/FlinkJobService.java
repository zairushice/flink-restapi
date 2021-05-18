package com.example.flink.restapi.service;

import com.example.flink.restapi.dto.FlinkJobDetailDTO;
import com.example.flink.restapi.dto.FlinkJobsDTO;
import com.example.flink.restapi.vo.JobVO;
import org.springframework.stereotype.Service;

public interface FlinkJobService {
    FlinkJobsDTO getFlinkJobs(String host);

    FlinkJobDetailDTO getFlinkJobDetail(JobVO jobVO);
}
