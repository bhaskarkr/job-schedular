package com.example.scheduler;

import com.example.db.hbase.TaskRepository;
import com.example.model.dto.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import io.dropwizard.lifecycle.Managed;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ClientTaskExtractor {
    private final Worker worker;
    private final ScheduledExecutorService executor;
    private ScheduledFuture<?> scheduledFuture;
    private final Client client;

    @Inject
    public ClientTaskExtractor(Client client,
                               TaskRepository taskRepository,
                               ObjectMapper objectMapper,
                               ScheduledExecutorService executor) {
        this.executor = executor;
        this.client = client;
        this.worker = Worker.builder()
                .clientId(client.getClientId())
                .taskRepository(taskRepository)
                .objectMapper(objectMapper)
                .build();
    }

    public void start() {
        log.info("ClientTaskExtractor started for client {}", client);
        scheduledFuture = executor.scheduleWithFixedDelay(worker, 10, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        scheduledFuture.cancel(false);
    }
}
