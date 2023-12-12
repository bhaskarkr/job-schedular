package com.example.rabbitmq;

import com.example.JobSchedulerConfiguration;
import com.example.model.dao.StoredTask;
import com.example.model.dto.Client;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import io.appform.dropwizard.actors.ConnectionRegistry;
import io.appform.dropwizard.actors.actor.ActorConfig;
import io.appform.dropwizard.actors.actor.MessageHandlingFunction;
import io.appform.dropwizard.actors.actor.MessageMetadata;
import io.appform.dropwizard.actors.actor.UnmanagedBaseActor;
import io.appform.dropwizard.actors.exceptionhandler.ExceptionHandlingFactory;
import io.appform.dropwizard.actors.retry.RetryStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;

import java.util.Set;

@Slf4j
public class ClientTaskActor {
    private static final Set<Class<? extends Exception>> IGNORABLE_EXCEPTIONS
            = ImmutableSet.of(JsonProcessingException.class);
    private UnmanagedBaseActor<StoredTask> actor;
    private final Client client;

    @Inject
    public ClientTaskActor(Client client,
                           ConnectionRegistry connectionRegistry,
                           ObjectMapper objectMapper,
                           ExceptionHandlingFactory exceptionHandlingFactory,
                           JobSchedulerConfiguration jobSchedulerConfiguration) {
        this.client = client;
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
    }

    private boolean handle(StoredTask storedTask, MessageMetadata messageMetadata) {
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
