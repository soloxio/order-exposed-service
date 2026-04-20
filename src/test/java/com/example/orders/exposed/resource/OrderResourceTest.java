package com.example.orders.exposed.resource;

import com.example.orders.exposed.client.OrchestrationClient;
import com.example.orders.exposed.dto.InternalOrderSearchDTO;
import com.example.orders.tmf622.model.ProductOrder;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDateTime;
import java.util.List;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class OrderResourceTest {

    private static final String BASE_PATH = "/nttdata/vds/productOrderingManagement/v1";

    @InjectMock
    @RestClient
    OrchestrationClient orchestrationClient;

    @Test
    void shouldRespondForInvalidPayload() {
        given()
                .contentType("application/json")
                .body("{}")
                .when()
                .post(BASE_PATH+ "/api/orders")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldSearchOrdersWithFullCriteria() {
        ProductOrder expected = new ProductOrder()
                .id("1")
                .externalId("EXT-123")
                .category("electronics")
                .description("test order")
                .state("CREATED");

        when(orchestrationClient.search(any())).thenReturn(List.of(expected));

        String startDate = "2025-05-01T10:00:00";
        String endDate = "2025-05-15T18:30:00";

        given()
                .queryParam("externalId", "EXT-123")
                .queryParam("state", "CREATED")
                .queryParam("page", 1)
                .queryParam("size", 10)
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .queryParam("sortBy", "orderDate,desc")
                .when()
                .get(BASE_PATH + "/api/orders")
                .then()
                .statusCode(200)
                .body("", hasSize(1))
                .body("[0].externalId", equalTo("EXT-123"));

        ArgumentCaptor<InternalOrderSearchDTO> captor = ArgumentCaptor.forClass(InternalOrderSearchDTO.class);
        Mockito.verify(orchestrationClient).search(captor.capture());
        InternalOrderSearchDTO captured = captor.getValue();
        assertEquals("EXT-123", captured.externalId());
        assertEquals("CREATED", captured.state());
        assertEquals(1, captured.page());
        assertEquals(10, captured.size());
        assertEquals(LocalDateTime.parse(startDate), captured.orderDateFrom());
        assertEquals(LocalDateTime.parse(endDate), captured.orderDateTo());
        assertEquals("orderDate,desc", captured.sortBy());
    }

    @Test
    void shouldSearchOrdersWithOnlyExternalId() {
        ProductOrder expected = new ProductOrder()
                .id("2")
                .externalId("ABC-999")
                .category("books")
                .description("single param test")
                .state("COMPLETED");

        when(orchestrationClient.search(any())).thenReturn(List.of(expected));

        given()
                .queryParam("externalId", "ABC-999")
                .queryParam("size", 5)
                .when()
                .get(BASE_PATH + "/api/orders")
                .then()
                .statusCode(200)
                .body("", hasSize(1))
                .body("[0].externalId", equalTo("ABC-999"));
    }

    @Test
    void shouldReturnInternalServerErrorWhenSizeIsZero() {
        given()
                .queryParam("externalId", "ABC-999")
                .queryParam("size", 0)
                .when()
                .get(BASE_PATH + "/api/orders")
                .then()
                .statusCode(500);
    }

    @Test
    void shouldReturnInternalServerErrorWhenClientThrows() {
        when(orchestrationClient.search(any())).thenThrow(new RuntimeException("service unavailable"));

        given()
                .queryParam("externalId", "EXT-123")
                .queryParam("size", 10)
                .when()
                .get(BASE_PATH + "/api/orders")
                .then()
                .statusCode(500);
    }

    @Test
    void shouldReturnInternalServerErrorWhenNoSizeAndNoOtherValidCriteriaProvided() {
        given()
                .when()
                .get(BASE_PATH + "/api/orders")
                .then()
                .statusCode(500);
    }
}
