package com.example.orders.exposed.dto;

public record InternalOrderResponse(
        String orderId,
        String state,
        String message
) {
}
