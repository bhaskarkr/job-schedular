package com.example.util;

import com.example.model.dao.StoredJob;
import com.example.model.request.ScheduleType;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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

    public static List<Date> getTaskExecutionTime(ScheduleType scheduleType,
                                                  List<Date> executionTimes,
                                                  long interval,
                                                  ChronoUnit unit,
                                                  Date endDate) {
        // TODO : Add visitor
        if(ScheduleType.REPEAT.equals(scheduleType) && !executionTimes.isEmpty()) {
            Date startDate = executionTimes.get(0);
            if(Objects.isNull(endDate)) {
                endDate = nextDate(startDate, 1, ChronoUnit.YEARS);
            }
            while(startDate.before(endDate)) {
                Date nextDate = nextDate(startDate, interval, unit);
                executionTimes.add(new Date(nextDate.getTime()));
                startDate = nextDate;
            }
        }
        return executionTimes;
    }
}
