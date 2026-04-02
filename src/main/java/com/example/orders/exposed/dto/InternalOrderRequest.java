package com.example.orders.exposed.dto;

import java.util.List;

public record InternalOrderRequest(
        String externalId,
        String category,
        String description,
        List<InternalOrderItemRequest> items
) {
}
