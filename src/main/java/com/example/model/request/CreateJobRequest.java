package com.example.model.request;

import com.example.model.request.schedule.ScheduleRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateJobRequest {
    private String clientId;
    private String url;
    private String callType;
    private String payload;
    private Map<String, String> headers;
    private ScheduleRequest scheduleRequest;
//    private ScheduleType scheduleType;
//    private List<Date> executionTime;
//    private Date endDate;
//    private long interval;
//    private ChronoUnit intervalUnit;
}

