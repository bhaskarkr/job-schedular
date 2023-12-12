package com.example;

import com.example.api.ClientApi;
import com.example.db.hbase.HBaseModule;
import com.example.rabbitmq.RabbitMQModule;
import com.google.inject.Stage;
import io.appform.dropwizard.actors.RabbitmqActorBundle;
import io.appform.dropwizard.actors.TtlConfig;
import io.appform.dropwizard.actors.config.RMQConfig;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class JobSchedulerApplication extends Application<JobSchedulerConfiguration> {

    private GuiceBundle guiceBundle;

    public static void main(final String[] args) throws Exception {
        new JobSchedulerApplication().run(args);
    }

    @Override
    public String getName() {
        return "jobscheduler";
    }

    @Override
    public void initialize(final Bootstrap<JobSchedulerConfiguration> bootstrap) {

        var rabbitmqActorBundle = new RabbitmqActorBundle<JobSchedulerConfiguration>() {
            @Override
            protected TtlConfig ttlConfig() {
                return TtlConfig.builder()
                        .ttlEnabled(false)
                        .build();
            }

            @Override
            protected RMQConfig getConfig(JobSchedulerConfiguration jobSchedulerConfiguration) {
                return jobSchedulerConfiguration.getRmqConfig();
            }
        };
        bootstrap.addBundle(rabbitmqActorBundle);

        this.guiceBundle = GuiceBundle.builder()
                .enableAutoConfig(getClass().getPackage().getName())
                .modules(
                        new HBaseModule(),
                        new CoreModule(),
                        new RabbitMQModule(rabbitmqActorBundle)
                )
                .build(Stage.PRODUCTION);
        bootstrap.addBundle(guiceBundle);
    }

    @Override
    public void run(final JobSchedulerConfiguration configuration,
                    final Environment environment) {
        var injector = guiceBundle.getInjector();
        environment.jersey().register(injector.getInstance(ClientApi.class));
    }

}
