package com.example.service;

import com.example.model.dto.Client;
import com.example.model.request.CreateClientRequest;

import java.util.List;

/**
 *  Author : Bhaskar Kumar
 *  Date : 9 Dec 2023
 */
public interface ClientService {
    List<Client> getAll();
    Client get(final String clientName);
    void save(final CreateClientRequest request);
}
