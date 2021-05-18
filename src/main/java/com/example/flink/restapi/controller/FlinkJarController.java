package com.example.flink.restapi.controller;

import com.example.flink.restapi.service.FlinkJarService;
import com.example.flink.restapi.vo.JarVO;
import com.example.flink.restapi.dto.FlinkJarSubmitDTO;
import com.example.flink.restapi.dto.FlinkJarsDTO;
import com.example.flink.restapi.dto.FlinkJarUploadDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/flink/api/jars")
public class FlinkJarController {

    @Resource
    FlinkJarService flinkJarService;

    @PostMapping
    public FlinkJarsDTO getFlinkJarsData(@RequestBody JarVO jarVO) {
        return flinkJarService.getJarsData(jarVO.getHost());
    }

    @PostMapping("/upload")
    public FlinkJarUploadDTO uploadFlinkJar(@RequestBody JarVO jarVO) {
        return flinkJarService.uploadJar(jarVO);
    }

    @PostMapping("/run")
    public FlinkJarSubmitDTO submitFlinkJar(@RequestBody JarVO jarVO) {
        return flinkJarService.runJar(jarVO);
    }

}
