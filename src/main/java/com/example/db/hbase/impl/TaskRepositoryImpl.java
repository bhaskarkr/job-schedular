package com.example.db.hbase.impl;

import com.example.db.hbase.HBaseClient;
import com.example.db.hbase.TaskRepository;
import com.example.model.dao.StoredTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import org.apache.hadoop.hbase.client.Table;

import java.util.List;

public class TaskRepositoryImpl implements TaskRepository {

    private static final String TASK_TABLE_NAME = "tasks";
    private static final byte[] TASK_META_DATA_CF = "meta".getBytes();
    private static final byte[] TASK_META_DATA = "data".getBytes();
    private final Table taskTable;
    private final ObjectMapper objectMapper;

    @Inject
    public TaskRepositoryImpl(HBaseClient hBaseClient,
                              ObjectMapper objectMapper) {
        this.taskTable = hBaseClient.getTable(TASK_TABLE_NAME);
        this.objectMapper = objectMapper;
    }

    @Override
    public Table getTable() {
        return taskTable;
    }

    @Override
    public List<StoredTask> getClientTasks(String clientName) {
        return null;
    }

    @Override
    public StoredTask get(final String clientName, final String jobId) {
        return null;
    }

    @Override
    public void save(StoredTask storedClient) {

    }
}
