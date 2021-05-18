package com.example.flink.restapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class FlinkJobsDTO {
    List<FlinkJob> jobs;
}
