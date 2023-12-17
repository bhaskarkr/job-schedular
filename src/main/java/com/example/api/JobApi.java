package com.example.api;

import com.example.model.dto.Client;
import com.example.model.dto.Job;
import com.example.model.request.CreateClientRequest;
import com.example.model.request.CreateJobRequest;
import com.example.service.ClientService;
import com.example.service.JobService;
import com.google.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

/**
 *  Author : Bhaskar Kumar
 *  Date : 9 Dec 2023
 */
@Path("/v1/jobs")
@Produces(MediaType.APPLICATION_JSON)
public class JobApi {

    private final JobService jobService;

    @Inject
    public JobApi(JobService jobService) {
        this.jobService = jobService;
    }

    @GET
    public List<Job> getAll() {
        return jobService.getAll();
    }

    @GET
    @Path("/{jobId}")
    public Job get(@NotBlank @PathParam("jobId") String clientId) {
        return jobService.get(clientId, "");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void save(CreateJobRequest request) {
        jobService.save(request);
    }
}
