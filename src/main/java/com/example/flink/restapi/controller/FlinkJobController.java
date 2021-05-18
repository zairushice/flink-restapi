package com.example.flink.restapi.controller;

import com.example.flink.restapi.dto.FlinkJobsDTO;
import com.example.flink.restapi.service.FlinkJobService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/flink/api/jobs")
public class FlinkJobController {

    @Resource
    FlinkJobService flinkJobService;

    @GetMapping
    public FlinkJobsDTO getFlinkJobsList() {
        return flinkJobService.getFlinkJobs();
    }
}
