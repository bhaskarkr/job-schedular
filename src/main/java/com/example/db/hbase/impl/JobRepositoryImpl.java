package com.example.db.hbase.impl;

import com.example.db.hbase.HBaseClient;
import com.example.db.hbase.JobRepository;
import com.example.model.dao.StoredJob;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;
import java.util.List;

public class JobRepositoryImpl implements JobRepository {

    private static final String JOB_TABLE_NAME = "jobs";
    private static final byte[] JOB_META_DATA_CF = "meta".getBytes();
    private static final byte[] JOB_META_DATA = "data".getBytes();
    private final Table jobTable;
    private final ObjectMapper objectMapper;

    @Inject
    public JobRepositoryImpl(HBaseClient hBaseClient,
                             ObjectMapper objectMapper) {
        this.jobTable = hBaseClient.getTable(JOB_TABLE_NAME);
        this.objectMapper = objectMapper;
    }

    @Override
    public Table getTable() {
        return jobTable;
    }

    @Override
    public List<StoredJob> getAll() {
        return null;
    }

    @Override
    public StoredJob get(String clientName) {
        Get get = new Get(clientName.getBytes());
        try {
            Result result = jobTable.get(get);
            if(result.isEmpty()) {
                throw new BadRequestException();
            }
            return objectMapper.readValue(result.getValue(JOB_META_DATA_CF, JOB_META_DATA), StoredJob.class);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(StoredJob storedJob) {
        try {
            Put put = new Put(storedJob.getClientId().getBytes());
            put.addColumn(JOB_META_DATA_CF, JOB_META_DATA, objectMapper.writeValueAsBytes(storedJob));
            jobTable.put(put);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
