package com.example.scheduler;

import com.example.model.dto.Client;
import com.example.service.ClientService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.dropwizard.lifecycle.Managed;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
@Slf4j
public class Scheduler implements Managed {
    private final ClientService clientService;

    @Inject
    public Scheduler(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void start() {
        log.info("Starting scheduler");
        List<Client> clients = clientService.getAll().stream()
                .filter(Client::isActive)
                .collect(Collectors.toList());
    }
}
