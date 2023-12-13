package com.example.model.request.schedule;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.temporal.ChronoUnit;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RepeatScheduleRequest extends ScheduleRequest {

    @NotNull
    private ChronoUnit intervalUnit;
    @NotNull
    private long interval;
    @NotNull
    private Date startExecutionDate;
    @NotNull
    private Date endExecutionDate;

    public RepeatScheduleRequest() {
        super(ScheduleType.REPEAT);
    }

    @Override
    public <T> T accept(ScheduleRequestVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
