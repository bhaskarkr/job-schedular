package com.example.model.request.schedule;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OnceScheduleRequest extends ScheduleRequest {

    @NotNull
    private Date executeAt;
    public OnceScheduleRequest() {
        super(ScheduleType.ONCE);
    }

    @Builder
    public OnceScheduleRequest(Date executeAt) {
        super(ScheduleType.ONCE);
        this.executeAt = executeAt;
    }

    @Override
    public <T> T accept(ScheduleRequestVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
