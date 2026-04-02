package com.example.orders.exposed.dto;

public record InternalOrderItemRequest(
        String lineId,
        String action,
        Integer quantity,
        String productOfferingId,
        String productOfferingName
) {
}
