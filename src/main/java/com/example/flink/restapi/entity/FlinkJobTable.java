package com.example.flink.restapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("flink_job")
public class FlinkJobTable {
    String jarId;
    @TableId(type = IdType.INPUT)
    String jobId;
    String status;
    String startTime;
    String endTime;
}
