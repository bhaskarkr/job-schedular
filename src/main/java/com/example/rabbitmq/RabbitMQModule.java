package com.example.rabbitmq;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.appform.dropwizard.actors.ConnectionRegistry;
import io.appform.dropwizard.actors.RabbitmqActorBundle;
import io.appform.dropwizard.actors.exceptionhandler.ExceptionHandlingFactory;

public class RabbitMQModule extends AbstractModule {
    private final RabbitmqActorBundle<?> rabbitmqActorBundle;

    public RabbitMQModule(RabbitmqActorBundle<?> rabbitmqActorBundle) {
        this.rabbitmqActorBundle = rabbitmqActorBundle;
    }

    @Singleton
    @Provides
    public ExceptionHandlingFactory provideExceptionHandlingFactory() {
        return new ExceptionHandlingFactory();
    }

    @Singleton
    @Provides
    public ConnectionRegistry provideConnectionRegistry() {
        return rabbitmqActorBundle.getConnectionRegistry();
    }
}
