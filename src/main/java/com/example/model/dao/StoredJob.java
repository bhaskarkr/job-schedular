package com.example.model.dao;

import com.example.model.request.schedule.CallType;
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
public class StoredJob {
    private String clientId;
    private boolean active;
    private String jobId;
    private String payload;
    private String url;
    private CallType callType;
    private Map<String, String> headers;
}
