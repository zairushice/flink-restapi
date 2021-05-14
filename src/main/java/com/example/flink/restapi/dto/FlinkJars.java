package com.example.flink.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlinkJars {
    String id;
    String name;
    Long uploaded;
    FlinkJarEntry entry;
}
