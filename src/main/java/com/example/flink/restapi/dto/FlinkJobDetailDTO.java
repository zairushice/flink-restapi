package com.example.flink.restapi.dto;

import lombok.Data;

@Data
public class FlinkJobDetailDTO {
    String jobId;
    String state;
    String startTime;
    String endTime;
}
