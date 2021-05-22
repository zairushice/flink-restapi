package com.example.flink.restapi.controller;

import com.example.flink.restapi.dto.RespDTO;
import com.example.flink.restapi.service.FlinkJarService;
import com.example.flink.restapi.dto.FlinkJarSubmitDTO;
import com.example.flink.restapi.dto.FlinkJarsDTO;
import com.example.flink.restapi.dto.FlinkJarUploadDTO;
import com.example.flink.restapi.vo.SubmitArgsVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/flink/api/jars")
public class FlinkJarController {

    @Resource
    FlinkJarService flinkJarService;

    @GetMapping
    public FlinkJarsDTO getFlinkJarsData() {
        return flinkJarService.getJarsData();
    }

    @PostMapping("/upload")
    public FlinkJarUploadDTO uploadFlinkJar(@RequestPart("file") MultipartFile file) {
        return flinkJarService.uploadJar(file);
    }

    @PostMapping("/{jarid}/run")
    public FlinkJarSubmitDTO submitFlinkJar(@PathVariable String jarid, @RequestBody SubmitArgsVO submitArgsVO) {
        return flinkJarService.runJar(jarid, submitArgsVO);
    }

    @DeleteMapping("/{jarid}")
    public RespDTO<?> deleteFlinkJar(@PathVariable String jarid) {
        return flinkJarService.deleteJar(jarid);
    }

}
