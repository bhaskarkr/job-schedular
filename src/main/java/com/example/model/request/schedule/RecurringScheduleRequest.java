package com.example.model.request.schedule;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RecurringScheduleRequest extends ScheduleRequest {

    @NotNull
    @NotEmpty
    private List<Date> executionDates;

    public RecurringScheduleRequest() {
        super(ScheduleType.RECURRING);
    }

    @Builder
    public RecurringScheduleRequest(List<Date> executionDates) {
        super(ScheduleType.RECURRING);
        this.executionDates = executionDates;
    }

    @Override
    public <T> T accept(ScheduleRequestVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
