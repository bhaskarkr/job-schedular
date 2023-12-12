package com.example.scheduler;

import com.example.db.hbase.TaskRepository;
import com.example.model.dto.Client;
import com.example.rabbitmq.ClientTaskActor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
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
    private final ClientTaskActor clientTaskActor;

    @Inject
    public ClientTaskExtractor(Client client,
                               TaskRepository taskRepository,
                               ObjectMapper objectMapper,
                               ScheduledExecutorService executor,
                               ClientTaskActor clientTaskActor) {
        this.executor = executor;
        this.client = client;
        this.clientTaskActor = clientTaskActor;
        this.worker = Worker.builder()
                .clientId(client.getClientId())
                .taskRepository(taskRepository)
                .clientTaskActor(clientTaskActor)
                .objectMapper(objectMapper)
                .build();
    }

    public void start() throws Exception {
        log.info("ClientTaskExtractor started for client {}", client);
        scheduledFuture = executor.scheduleWithFixedDelay(worker, 10, 10, TimeUnit.SECONDS);
        clientTaskActor.start();
    }

    public void stop() throws Exception {
        scheduledFuture.cancel(false);
        clientTaskActor.stop();
    }
}
