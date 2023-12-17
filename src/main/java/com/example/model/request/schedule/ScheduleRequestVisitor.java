package com.example.model.request.schedule;

public interface ScheduleRequestVisitor<T> {
    T visit(OnceScheduleRequest request);
    T visit(RecurringScheduleRequest request);
    T visit(RepeatScheduleRequest request);
}
