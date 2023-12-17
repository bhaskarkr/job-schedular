package com.example.db.hbase;

import com.example.model.dao.StoredClient;
import com.example.model.dao.StoredJob;
import org.apache.hadoop.hbase.client.Table;

import java.util.List;

public interface JobRepository {
    Table getTable();
    List<StoredJob> getAll();
    StoredJob get(final String clientName);
    void save(StoredJob storedJob);
}
