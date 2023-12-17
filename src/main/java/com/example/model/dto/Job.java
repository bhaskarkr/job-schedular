package com.example.model.dto;

import com.example.model.request.schedule.CallType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {
    private String clientId;
    private String jobId;
    private List<String> taskIds;
    private boolean active;
    private String payload;
    private String url;
    private CallType callType;
    private Map<String, String> headers;
}