package com.example.flink.restapi.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class FlinkJarsDTO {
    String address;
    List<FlinkJars> files;
}
