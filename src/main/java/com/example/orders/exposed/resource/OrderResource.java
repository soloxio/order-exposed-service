package com.example.orders.exposed.resource;

import com.example.orders.exposed.client.OrchestrationClient;
import com.example.orders.exposed.dto.InternalOrderRequest;
import com.example.orders.exposed.dto.InternalOrderResponse;
import com.example.orders.exposed.dto.InternalOrderSearchDTO;
import com.example.orders.exposed.mapper.ProductOrderMapper;
import com.example.orders.tmf622.model.ProductOrder;
import com.example.orders.tmf622.model.ProductOrderCreate;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    try {
      if (request == null || request.getId() == null) {
        return Response.status(Response.Status.BAD_REQUEST).build();
      }
      InternalOrderRequest internalRequest = productOrderMapper.toInternal(request);
      InternalOrderResponse response = orchestrationClient.submit(internalRequest);

      return Response.accepted(response).build();
    } catch (Exception e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Failed to process order: " + e.getMessage())
          .build();
    }
  }

  @GET
  public Response searchOrders(@QueryParam("externalId") Optional<String> externalId,
      @QueryParam("state") Optional<String> state,
      @QueryParam("page") Optional<Integer> page,
      @QueryParam("size") Optional<Integer> size,
      @QueryParam("startDate") Optional<LocalDateTime> startDate,
      @QueryParam("endDate") Optional<LocalDateTime> endDate,
      @QueryParam("sortBy") Optional<String> sortBy) {
    try {
      InternalOrderSearchDTO internalOrderSearchDTO = new InternalOrderSearchDTO(externalId.orElse(null),
          state.orElse(null), page.orElse(0), size.orElse(0), startDate.orElse(null), endDate.orElse(null),
          sortBy.orElse(""));
      List<ProductOrder> productOrders = orchestrationClient.search(internalOrderSearchDTO);
      return Response.ok(productOrders).build();
    } catch (Exception e) {
      if (e instanceof IllegalArgumentException) {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity("Invalid search parameters: " + e.getMessage())
            .build();
      }
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Search Order failed due to exception: " + e.getMessage()).build();
    }
  }
}
