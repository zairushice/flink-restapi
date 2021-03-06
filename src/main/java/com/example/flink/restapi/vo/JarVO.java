package com.example.flink.restapi.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
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
