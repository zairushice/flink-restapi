package com.example.flink.restapi.api.service;


import com.example.flink.restapi.api.vo.JarVO;
import com.example.flink.restapi.dto.FlinkJarSubmitDTO;
import com.example.flink.restapi.dto.FlinkJarsDTO;
import com.example.flink.restapi.dto.FlinkJarUploadDTO;

public interface FlinkJarService {

    FlinkJarsDTO getJarsData(String host);

    FlinkJarUploadDTO uploadJar(JarVO jarVO);

    FlinkJarSubmitDTO runJar(JarVO jarVO);
}
