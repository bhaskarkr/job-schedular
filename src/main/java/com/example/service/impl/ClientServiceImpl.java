package com.example.service.impl;

import com.example.db.hbase.ClientRepository;
import com.example.util.HBaseUtil;
import com.example.model.dto.Client;
import com.example.model.request.CreateClientRequest;
import com.example.service.ClientService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;
import java.util.stream.Collectors;
/**
 *  Author : Bhaskar Kumar
 *  Date : 9 Dec 2023
 */
@Singleton
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Inject
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    @Override
    public List<Client> getAll() {
        return clientRepository.getAll().stream()
                .map(HBaseUtil::dto)
                .collect(Collectors.toList());
    }

    @Override
    public Client get(String clientName) {
        return HBaseUtil.dto(clientRepository.get(clientName));
    }

    @Override
    public void save(CreateClientRequest request) {
        clientRepository.save(HBaseUtil.dao(request));
    }
}
