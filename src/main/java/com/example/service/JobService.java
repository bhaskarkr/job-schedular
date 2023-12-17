package com.example.service;

import com.example.model.dao.StoredJob;
import com.example.model.dto.Client;
import com.example.model.dto.Job;
import com.example.model.request.CreateClientRequest;
import com.example.model.request.CreateJobRequest;

import java.util.List;

/**
 *  Author : Bhaskar Kumar
 *  Date : 9 Dec 2023
 */
public interface JobService {
    List<Job> getAll();
    Job get(final String clientId, final String jobId);
    void save(final CreateJobRequest request);
}
