package com.example.service.impl;

import com.example.db.hbase.JobRepository;
import com.example.db.hbase.TaskRepository;
import com.example.model.dao.StoredJob;
import com.example.model.dao.StoredTask;
import com.example.model.dto.Job;
import com.example.model.request.CreateJobRequest;
import com.example.service.JobService;
import com.example.util.JobUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Put;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *  Author : Bhaskar Kumar
 *  Date : 9 Dec 2023
 */
@Singleton
@Slf4j
public class JobServiceImpl implements JobService {

    private static final byte[] TASK_META_DATA_CF = "meta".getBytes();
    private static final byte[] JOB_META_DATA_CF = "meta".getBytes();
    private static final byte[] TASK_META_DATA = "data".getBytes();
    private static final byte[] JOB_META_DATA = "data".getBytes();
    private static final byte[] TASK_META_JOB_ID = "jobId".getBytes();
    private static final byte[] JOB_TASK_CF = "tasks".getBytes();
    private final JobRepository jobRepository;
    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;

    @Inject
    public JobServiceImpl(JobRepository jobRepository, TaskRepository taskRepository, ObjectMapper objectMapper) {
        this.jobRepository = jobRepository;
        this.taskRepository = taskRepository;
        this.objectMapper = objectMapper;
    }
    @Override
    public List<Job> getAll() {
        return List.of();
    }

    @Override
    public Job get(String clientId, String jobId) {
        return null;
    }

    private byte[] getTaskRowKey(String clientId, String jobId, Date executionDate) {
        String key = String.format("%s:%015d:%s", clientId, executionDate.getTime(), jobId);
        return key.getBytes();
    }

    @Override
    public void save(CreateJobRequest request) {
        List<Date> executionDates = JobUtil.getTaskExecutionTime(request.getScheduleType(),
                request.getExecutionTime(), request.getInterval(), request.getIntervalUnit(), request.getEndDate());
        List<Put> taskPutList = new ArrayList<>();
        StoredJob storedJob = StoredJob.builder()
                .clientId(request.getClientId())
                .active(true)
                .jobId(UUID.randomUUID().toString())
                .build();
        String jobRowKey = request.getClientId() + ":" + storedJob.getJobId();
        Put jobPut = new Put(jobRowKey.getBytes());
        try {
            jobPut.addColumn(JOB_META_DATA_CF, JOB_META_DATA, objectMapper.writeValueAsBytes(storedJob));
            executionDates.forEach(executionDate -> {
                StoredTask storedTask = StoredTask.builder()
                        .taskId(UUID.randomUUID().toString())
                        .jobId(storedJob.getJobId())
                        .executeAt(executionDate)
                        .clientId(request.getClientId())
                        .build();
                log.info(executionDate.toString());
                try {
                    Put taskPut = new Put(getTaskRowKey(request.getClientId(), storedJob.getJobId(), executionDate))
                            .addColumn(TASK_META_DATA_CF, TASK_META_JOB_ID, storedJob.getJobId().getBytes())
                            .addColumn(TASK_META_DATA_CF, TASK_META_DATA, objectMapper.writeValueAsBytes(storedTask));
                    jobPut.addColumn(JOB_TASK_CF, storedTask.getTaskId().getBytes(), objectMapper.writeValueAsBytes(storedTask));
                    taskPutList.add(taskPut);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
            jobRepository.getTable().put(jobPut);
            taskRepository.getTable().put(taskPutList);
        } catch (IOException e) {
            // do nothing
        }
    }
}
