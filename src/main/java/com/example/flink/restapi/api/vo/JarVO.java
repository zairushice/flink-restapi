package com.example.flink.restapi.api.vo;

import lombok.Data;


@Data
public class JarVO {
    String host;
    String filename;
    String filepath;
    String jarId;
    String programArgs;
    String savePointPath;
    String entryClass;
    String parallelism;
}
