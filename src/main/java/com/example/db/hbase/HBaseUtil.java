package com.example.db.hbase;

import com.example.model.dao.StoredClient;
import com.example.model.dto.Client;
import com.example.model.request.CreateClientRequest;

/**
 *  Author : Bhaskar Kumar
 *  Date : 9 Dec 2023
 */
public abstract class HBaseUtil {
    public static StoredClient dao(CreateClientRequest request) {
        return StoredClient.builder()
                .clientId(request.getClientId())
                .active(true)
                .build();
    }

    public static Client dto(StoredClient storedClient) {
        return Client.builder()
                .clientId(storedClient.getClientId())
                .active(storedClient.isActive())
                .build();
    }

}
