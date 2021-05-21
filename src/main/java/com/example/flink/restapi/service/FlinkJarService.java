package com.example.flink.restapi.service;


import com.example.flink.restapi.dto.FlinkJarSubmitDTO;
import com.example.flink.restapi.dto.FlinkJarsDTO;
import com.example.flink.restapi.dto.FlinkJarUploadDTO;
import com.example.flink.restapi.vo.SubmitArgsVO;
import org.springframework.web.multipart.MultipartFile;

public interface FlinkJarService {

    FlinkJarsDTO getJarsData();

    FlinkJarUploadDTO uploadJar(MultipartFile file);

    FlinkJarSubmitDTO runJar(String jarid, SubmitArgsVO submitArgsVO);
}
