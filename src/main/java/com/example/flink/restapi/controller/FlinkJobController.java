package com.example.flink.restapi.controller;

import com.example.flink.restapi.dto.FlinkJobDetailDTO;
import com.example.flink.restapi.dto.FlinkJobsDTO;
import com.example.flink.restapi.service.FlinkJobService;
import com.example.flink.restapi.vo.JobVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/flink/api/jobs")
public class FlinkJobController {

    @Resource
    FlinkJobService flinkJobService;

    @PostMapping
    public FlinkJobsDTO getFlinkJobsList(@RequestBody JobVO jobVo) {
        return flinkJobService.getFlinkJobs(jobVo.getHost());
    }

    @PostMapping("/detail")
    public FlinkJobDetailDTO getFlinkJobDetail(@RequestBody JobVO jobVO) {
        return flinkJobService.getFlinkJobDetail(jobVO);
    }
}
