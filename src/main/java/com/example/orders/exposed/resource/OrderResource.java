package com.example.orders.exposed.resource;

import com.example.orders.exposed.client.OrchestrationClient;
import com.example.orders.exposed.dto.InternalOrderRequest;
import com.example.orders.exposed.dto.InternalOrderResponse;
import com.example.orders.exposed.mapper.ProductOrderMapper;
import com.example.orders.tmf622.model.ProductOrderCreate;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/api/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    ProductOrderMapper productOrderMapper;

    @Inject
    @RestClient
    OrchestrationClient orchestrationClient;

    @POST
    public Response createOrder(ProductOrderCreate request) {
        InternalOrderRequest internalRequest = productOrderMapper.toInternal(request);
        InternalOrderResponse response = orchestrationClient.submit(internalRequest);
        return Response.accepted(response).build();
    }
}
