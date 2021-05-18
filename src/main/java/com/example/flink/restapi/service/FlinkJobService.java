package com.example.flink.restapi.service;

import com.example.flink.restapi.dto.FlinkJobsDTO;
import org.springframework.stereotype.Service;

@Service
public interface FlinkJobService {
    FlinkJobsDTO getFlinkJobs();


}
