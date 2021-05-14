package com.example.flink.restapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("flink_jar")
public class FlinkJar {
    @TableId(type = IdType.INPUT)
    String jarId;
    String fileName;
    String uploadTime;
    String hostName;
}
