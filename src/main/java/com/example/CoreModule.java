package com.example;

import com.example.service.ClientService;
import com.example.service.JobService;
import com.example.service.impl.ClientServiceImpl;
import com.example.service.impl.JobServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

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
    public ObjectMapper provideObjectMapper() {
        return new ObjectMapper();
    }
}
