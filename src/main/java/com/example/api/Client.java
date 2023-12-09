package com.example.api;

import com.example.db.hbase.ClientRepository;
import com.example.db.model.StoredClient;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

/**
 *  Author : Bhaskar Kumar
 *  Date : 7 Dec 2023
 */
@Path("/v1/clients")
@Produces(MediaType.APPLICATION_JSON)
public class Client {

    private final ClientRepository clientRepository;

    @Inject
    public Client(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    @GET
    public List<StoredClient> getAll() {
        return clientRepository.getAll();
    }

    @GET
    @Path("/{clientId}")
    public String get(@NotBlank @PathParam("clientId") String clientId) {
        return clientId;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void save(StoredClient storedClient) {
        clientRepository.save(storedClient);
    }
}
