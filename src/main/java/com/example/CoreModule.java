package com.example;

import com.example.service.ClientService;
import com.example.service.JobService;
import com.example.service.impl.ClientServiceImpl;
import com.example.service.impl.JobServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.lifecycle.setup.ScheduledExecutorServiceBuilder;

import java.util.concurrent.ScheduledExecutorService;

/**
 *  Author : Bhaskar Kumar
 *  Date : 9 Dec 2023
 */
public class CoreModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ClientService.class).to(ClientServiceImpl.class);
        bind(JobService.class).to(JobServiceImpl.class);
    }

    @Provides
    public ObjectMapper provideObjectMapper(Environment environment) {
        return environment.getObjectMapper();
    }

    @Provides
    public ScheduledExecutorService provideScheduledExecutorService(Environment environment) {
        return environment.lifecycle().scheduledExecutorService("job-scheduler").build();
    }
}
