package com.example.flink.restapi.constant;

public interface RestApiConstant {
    // api on jars
    String listJars = "/jars";
    String uploadJar = "/jars/upload";
    String deleteJar = "/jars/{jarid}";
    String showPlan = "/jars/{jarid}/plan";
    String runJar = "/jars/{jarid}/run";
    // api on jobManager
    String getClusterConfig = "/jobmanager/config";
    String getLogFileList = "/jobmanager/logs";
    String getJobManagerLogFile = "/jobmanager/logs/{logfilename}";
    String getJobManagerLog = "/jobmanager/log";
    String getJobManagerStdOut = "/jobmanager/stdout";
    // api on jobs
    String getJobsInfo = "/jobs";
    String getJobsOverview = "/jobs/overview";
    String getJobDetail = "/jobs/{jobId}";
    String terminateJob = "/jobs/{jobId}";
    String getJobAccumulators = "/jobs/{jobid}/accumulators";
    String getJobCheckpoints = "/jobs/{jobid}/checkpoints";
    String getJobCheckpointsConfig = "/jobs/{jobId}/checkpoints/config";
    String getJobCheckpointsDetail = "/jobs/{jobId}/checkpoints/details/{checkpointId}";
}
