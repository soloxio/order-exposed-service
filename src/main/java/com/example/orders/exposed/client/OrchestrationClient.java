package com.example.orders.exposed.client;

import com.example.orders.exposed.dto.InternalOrderRequest;
import com.example.orders.exposed.dto.InternalOrderResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/internal/orders")
@RegisterRestClient(configKey = "orchestration-api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface OrchestrationClient {

    @POST
    InternalOrderResponse submit(InternalOrderRequest request);
}
