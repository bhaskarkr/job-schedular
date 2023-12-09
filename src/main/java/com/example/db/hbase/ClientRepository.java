package com.example.db.hbase;

import com.example.db.model.StoredClient;

import java.util.List;

public interface ClientRepository {
    List<StoredClient> getAll();
    StoredClient get(final String clientName);
    void save(StoredClient storedClient);
}
