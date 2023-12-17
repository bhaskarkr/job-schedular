package com.example.util;

import com.example.model.dao.StoredClient;
import com.example.model.dao.StoredJob;
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

    public static StoredJob dao(String clientId) {
        return StoredJob.builder()
                .clientId(clientId)
                .active(true)
                .build();
    }


}
