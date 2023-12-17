package com.example.model.request.schedule;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "scheduleType")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "ONCE", value = OnceScheduleRequest.class),
        @JsonSubTypes.Type(name = "RECURRING", value = RecurringScheduleRequest.class),
        @JsonSubTypes.Type(name = "REPEAT", value = RepeatScheduleRequest.class)
})
public abstract class ScheduleRequest {

    private final ScheduleType scheduleType;

    protected ScheduleRequest(ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
    }

    public abstract <T> T accept(ScheduleRequestVisitor<T> visitor);
}
