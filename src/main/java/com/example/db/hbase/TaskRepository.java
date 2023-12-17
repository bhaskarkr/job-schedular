package com.example.db.hbase;

import com.example.model.dao.StoredClient;
import com.example.model.dao.StoredTask;
import org.apache.hadoop.hbase.client.Table;

import java.util.List;

public interface TaskRepository {
    Table getTable();
    List<StoredTask> getClientTasks(final String clientName);
    StoredTask get(final String clientName, final String jobId);
    void save(final StoredTask storedTask);
}
