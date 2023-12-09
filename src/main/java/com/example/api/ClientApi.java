package com.example.api;

import com.example.model.dto.Client;
import com.example.model.request.CreateClientRequest;
import com.example.service.ClientService;
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
public class ClientApi {

    private final ClientService clientService;

    @Inject
    public ClientApi(ClientService clientService) {
        this.clientService = clientService;
    }

    @GET
    public List<Client> getAll() {
        return clientService.getAll();
    }

    @GET
    @Path("/{clientId}")
    public Client get(@NotBlank @PathParam("clientId") String clientId) {
        return clientService.get(clientId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void save(CreateClientRequest request) {
        clientService.save(request);
    }
}
