package com.example;

import com.example.api.Client;
import com.example.db.hbase.HBaseModule;
import com.google.inject.Stage;
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
        this.guiceBundle = GuiceBundle.builder()
                .enableAutoConfig(getClass().getPackage().getName())
                .modules(
                        new HBaseModule(),
                        new CoreModule()
                )
                .build(Stage.PRODUCTION);
        bootstrap.addBundle(guiceBundle);
    }

    @Override
    public void run(final JobSchedulerConfiguration configuration,
                    final Environment environment) {
        var injector = guiceBundle.getInjector();
        environment.jersey().register(injector.getInstance(Client.class));
    }

}
