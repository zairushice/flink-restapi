package com.example.flink.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlinkJob {
    String id;
    String status;
}
