package com.example.scheduler;

import com.example.db.hbase.TaskRepository;
import com.example.model.config.WorkerScanConfig;
import com.example.model.dto.Client;
import com.example.rabbitmq.ClientTaskActor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class ClientTaskExtractor {
    private final List<Worker> workers;
    private final ScheduledExecutorService executor;
    private List<ScheduledFuture<?>> scheduledFutures;
    private final Client client;
    private final ClientTaskActor clientTaskActor;

    @Inject
    public ClientTaskExtractor(Client client,
                               TaskRepository taskRepository,
                               ObjectMapper objectMapper,
                               ScheduledExecutorService executor,
                               ClientTaskActor clientTaskActor,
                               WorkerScanConfig workerScanConfig) {
        this.executor = executor;
        this.client = client;
        this.clientTaskActor = clientTaskActor;
        this.workers = workerScanConfig.getScanConfigList().stream().map(scanConfig ->
            Worker.builder()
                    .clientId(client.getClientId())
                    .taskRepository(taskRepository)
                    .clientTaskActor(clientTaskActor)
                    .objectMapper(objectMapper)
                    .scanConfig(scanConfig)
                    .build()
        ).collect(Collectors.toList());
    }

    public void start() throws Exception {
        log.info("ClientTaskExtractor started for client {}", client);
        scheduledFutures = workers.stream()
                .map(worker -> executor.scheduleWithFixedDelay(worker, 10, worker.getScanConfig().getInterval(), TimeUnit.of(worker.getScanConfig().getChronoUnit())))
                .collect(Collectors.toList());
        clientTaskActor.start();
    }

    public void stop() throws Exception {
        scheduledFutures.forEach(scheduledFuture -> scheduledFuture.cancel(false));
        clientTaskActor.stop();
    }
}
