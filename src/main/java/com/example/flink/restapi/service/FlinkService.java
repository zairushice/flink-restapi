package com.example.flink.restapi.service;

import com.alibaba.fastjson.JSONObject;
import com.example.flink.restapi.vo.SubmitArgsVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@FeignClient(name = "flink-client", url = "${flink-client.host}:${flink-client.port}")
public interface FlinkService {

    @PostMapping(value = "/jars/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    JSONObject upload(@RequestPart("file")MultipartFile file);

    @GetMapping(value = "/jars")
    JSONObject showJars();

    @PostMapping(value = "/jars/{jarid}/run", consumes = MediaType.APPLICATION_JSON_VALUE)
    JSONObject runJar(@PathVariable String jarid, @RequestBody Map<String, String> args);

    @DeleteMapping(value = "/jars/{jarid}")
    JSONObject deleteJar(@PathVariable String jarid);
}
