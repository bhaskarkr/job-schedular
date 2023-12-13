package com.example.util;

import com.example.model.request.schedule.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *  Author : Bhaskar Kumar
 *  Date : 9 Dec 2023
 */
public abstract class JobUtil {
    public static Date nextDate(Date currentDate, long interval, ChronoUnit unit) {
        LocalDateTime local = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime future = local.plus(interval, unit);
        return Date.from(future.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static List<Date> getTaskExecutionTime(ScheduleRequest scheduleRequest) {
        return scheduleRequest.accept(new ScheduleRequestVisitor<List<Date>>() {
            @Override
            public List<Date> visit(OnceScheduleRequest request) {
                return List.of(request.getExecuteAt());
            }

            @Override
            public List<Date> visit(RecurringScheduleRequest request) {
                return request.getExecutionDates();
            }

            @Override
            public List<Date> visit(RepeatScheduleRequest request) {
                List<Date> executionTimes = new ArrayList<>();
                Date startDate = request.getStartExecutionDate();
                while(startDate.before(request.getEndExecutionDate())) {
                    Date nextDate = nextDate(startDate, request.getInterval(), request.getIntervalUnit());
                    executionTimes.add(new Date(nextDate.getTime()));
                    startDate = nextDate;
                }
                return executionTimes;
            }
        });
    }
}
