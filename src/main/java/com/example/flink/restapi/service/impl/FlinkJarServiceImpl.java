package com.example.flink.restapi.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.flink.restapi.dao.FlinkJobDAO;
import com.example.flink.restapi.service.FlinkJobService;
import com.example.flink.restapi.service.FlinkService;
import com.example.flink.restapi.dao.FlinkJarDAO;
import com.example.flink.restapi.dto.*;
import com.example.flink.restapi.service.FlinkJarService;
import com.example.flink.restapi.vo.SubmitArgsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

@Service
@Slf4j
public class FlinkJarServiceImpl implements FlinkJarService {

    @Resource
    FlinkJarDAO flinkJarDAO;

    @Resource
    FlinkJobDAO flinkJobDAO;

    @Resource
    FlinkJobService flinkJobService;

    @Autowired
    FlinkService flinkService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public FlinkJarsDTO getJarsData() {
        JSONObject jsonObject = flinkService.showJars();
        FlinkJarsDTO flinkJarsDTO = new FlinkJarsDTO();
        flinkJarsDTO.setAddress(jsonObject.getString("address"));
        JSONArray files = jsonObject.getJSONArray("files");
        int fileCount = files.size();
        if (fileCount != 0) {
            ArrayList<FlinkJars> jars = new ArrayList<>();
            for (int i = 0; i < fileCount; i++) {
                JSONObject jar = files.getJSONObject(i);
                String id = jar.getString("id");
                String name = jar.getString("name");
                Long uploaded = jar.getLong("uploaded");
                JSONArray entryArray = jar.getJSONArray("entry");
                FlinkJarEntry ent = new FlinkJarEntry();
                if (entryArray.size() != 0) {
                    JSONObject entry = entryArray.getJSONObject(0);
                    ent.setName(entry.getString("name"));
                    ent.setDescription(entry.getString("description"));
                }
                jars.add(new FlinkJars(id, name, uploaded, ent));
            }
            flinkJarsDTO.setFiles(jars);
        }
        return flinkJarsDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FlinkJarUploadDTO uploadJar(MultipartFile file) {
        FlinkJarUploadDTO flinkJarUploadDTO = new FlinkJarUploadDTO();
        JSONObject res = flinkService.upload(file);
        String fn = res.getString("filename");
        String status = res.getString("status");
        flinkJarUploadDTO.setFilename(fn);
        flinkJarUploadDTO.setStatus(status);
        return flinkJarUploadDTO;
    }

    @Override
    public FlinkJarSubmitDTO runJar(String jarid, SubmitArgsVO submitArgsVO) {
        FlinkJarSubmitDTO flinkJarSubmitDTO = new FlinkJarSubmitDTO();
        HashMap<String, String> hashMap = new HashMap<>();
        if (submitArgsVO.getProgramArgs() != null) {
            hashMap.put("programArgs", submitArgsVO.getProgramArgs());
        }
        if (submitArgsVO.getParallelism() != null) {
            hashMap.put("parallelism", submitArgsVO.getParallelism());
        }
        if (submitArgsVO.getEntryClass() != null) {
            hashMap.put("entry-class", submitArgsVO.getEntryClass());
        }
        if (submitArgsVO.getSavePointPath() != null) {
            hashMap.put("savepointPath", submitArgsVO.getSavePointPath());
        }
        JSONObject jsonObject = flinkService.runJar(jarid, hashMap);
        String jobId = jsonObject.getString("jobid");
        flinkJarSubmitDTO.setJobId(jobId);
        return flinkJarSubmitDTO;
    }
}
