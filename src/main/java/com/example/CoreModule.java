package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class CoreModule extends AbstractModule {
    @Provides
    public ObjectMapper provideObjectMapper() {
        return new ObjectMapper();
    }
}
