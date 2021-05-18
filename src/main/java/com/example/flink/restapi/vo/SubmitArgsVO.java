package com.example.flink.restapi.vo;

import lombok.Data;

@Data
public class SubmitArgsVO {
    String programArgs;
    String savePointPath;
    String entryClass;
    String parallelism;
}
