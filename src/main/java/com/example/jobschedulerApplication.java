package com.example;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

public class jobschedulerApplication extends Application<jobschedulerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new jobschedulerApplication().run(args);
    }

    @Override
    public String getName() {
        return "jobscheduler";
    }

    @Override
    public void initialize(final Bootstrap<jobschedulerConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final jobschedulerConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
