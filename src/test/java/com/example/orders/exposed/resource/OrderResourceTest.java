package com.example.orders.exposed.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class OrderResourceTest {

    @Test
    void shouldRespondForInvalidPayload() {
        given()
                .contentType("application/json")
                .body("{}")
        .when()
                .post("/api/orders")
        .then()
                .statusCode(500);
    }
}
