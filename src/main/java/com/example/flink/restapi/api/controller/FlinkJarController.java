package com.example.flink.restapi.api.controller;

import com.example.flink.restapi.api.service.FlinkJarService;
import com.example.flink.restapi.api.vo.JarVO;
import com.example.flink.restapi.dto.FlinkJarSubmitDTO;
import com.example.flink.restapi.dto.FlinkJarsDTO;
import com.example.flink.restapi.dto.FlinkJarUploadDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/flink/api")
public class FlinkJarController {

    @Resource
    FlinkJarService flinkJarService;

    @PostMapping("/jars")
    public FlinkJarsDTO getFlinkJarsData(@RequestBody JarVO jarVO) {
        return flinkJarService.getJarsData(jarVO.getHost());
    }

    @PostMapping("/jars/upload")
    public FlinkJarUploadDTO uploadFlinkJar(@RequestBody JarVO jarVO) {
        return flinkJarService.uploadJar(jarVO);
    }

    @PostMapping("/jars/run")
    public FlinkJarSubmitDTO submitFlinkJar(@RequestBody JarVO jarVO) {
        return flinkJarService.runJar(jarVO);
    }

}
