package com.example.rabbitmq;

import com.example.JobSchedulerConfiguration;
import com.example.db.hbase.JobRepository;
import com.example.db.hbase.TaskRepository;
import com.example.model.dao.StoredJob;
import com.example.model.dao.StoredTask;
import com.example.model.dto.Client;
import com.example.model.request.schedule.HttpCallRequest;
import com.example.scheduler.ClientHttpCallHandler;
import com.example.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import io.appform.dropwizard.actors.ConnectionRegistry;
import io.appform.dropwizard.actors.actor.ActorConfig;
import io.appform.dropwizard.actors.actor.MessageMetadata;
import io.appform.dropwizard.actors.actor.UnmanagedBaseActor;
import io.appform.dropwizard.actors.exceptionhandler.ExceptionHandlingFactory;
import io.appform.dropwizard.actors.retry.RetryStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;
import java.util.Set;

@Slf4j
public class ClientTaskActor {
    private static final Set<Class<? extends Exception>> IGNORABLE_EXCEPTIONS
            = ImmutableSet.of(JsonProcessingException.class);
    private UnmanagedBaseActor<StoredTask> actor;
    private final JobRepository jobRepository;
    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;
    private final ClientHttpCallHandler clientHttpCallHandler;

    @Inject
    public ClientTaskActor(Client client,
                           ConnectionRegistry connectionRegistry,
                           ObjectMapper objectMapper,
                           JobRepository jobRepository,
                           TaskRepository taskRepository,
                           ExceptionHandlingFactory exceptionHandlingFactory,
                           JobSchedulerConfiguration jobSchedulerConfiguration,
                           ClientHttpCallHandler clientHttpCallHandler) {
        this.jobRepository = jobRepository;
        this.taskRepository = taskRepository;
        this.objectMapper = objectMapper;
        this.actor = new UnmanagedBaseActor<StoredTask>(
                client.getClientId(),
                ActorConfig.builder().exchange(client.getClientId()).build(),
                connectionRegistry,
                objectMapper,
                new RetryStrategyFactory(),
                exceptionHandlingFactory,
                StoredTask.class,
                this::handle,
                this::handle,
                t -> IGNORABLE_EXCEPTIONS.stream().anyMatch(ex -> ClassUtils.isAssignable(t.getClass(), ex))
        );
        this.clientHttpCallHandler = clientHttpCallHandler;
    }

    private boolean handle(StoredTask storedTask, MessageMetadata messageMetadata) {
        Table table = jobRepository.getTable();
        String jobRowKey = storedTask.getClientId() + ":" + storedTask.getJobId();
        Get get = new Get(jobRowKey.getBytes());
        try {
            Result result = table.get(get);
            StoredJob storedJob = objectMapper.readValue(result.getValue(Constants.JOB_META_DATA_CF,
                    Constants.JOB_META_DATA), StoredJob.class);
            HttpCallRequest httpCallRequest = HttpCallRequest.builder()
                    .callType(storedJob.getCallType())
                    .url(storedJob.getUrl())
                    .payload(storedJob.getPayload())
                    .headers(storedJob.getHeaders())
                    .build();
            clientHttpCallHandler.executeCall(httpCallRequest);
            byte[] deleteTaskRowKey = String.format("%s:%015d:%s", storedTask.getClientId(), storedTask.getExecuteAt().getTime(), storedTask.getJobId()).getBytes();
            Delete delete = new Delete(deleteTaskRowKey);
            log.info("Deleted row {}", deleteTaskRowKey);
            taskRepository.getTable().delete(delete);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        log.info("Handler for {}", storedTask);
        return true;
    }

    public void publish(String clientId, StoredTask storedTask) throws Exception {
        actor.publish(storedTask);
    }

    public void start() throws Exception {
        actor.start();
    }

    public void stop() throws Exception {
        actor.stop();
    }
}
