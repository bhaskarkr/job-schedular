package com.example.api;

import com.google.common.collect.ImmutableList;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

/**
 *  Author : Bhaskar Kumar
 *  Date : 7 Dec 2023
 */
@Path("/v1/clients")
@Produces(MediaType.APPLICATION_JSON)
public class Client {

    @GET
    public List<String> getAll() {
        return ImmutableList.of("Client 1");
    }

    @GET
    @Path("/{clientId}")
    public String get(@NotBlank @PathParam("clientId") String clientId) {
        return clientId;
    }
}
