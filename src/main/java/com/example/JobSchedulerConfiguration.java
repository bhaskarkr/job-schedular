package com.example;

import com.example.model.config.WorkerScanConfig;
import io.appform.dropwizard.actors.config.RMQConfig;
import io.dropwizard.core.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.*;
import jakarta.validation.constraints.*;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JobSchedulerConfiguration extends Configuration {
    @NotNull
    private RMQConfig rmqConfig;
    @NotNull
    private WorkerScanConfig workerScanConfig;
}
