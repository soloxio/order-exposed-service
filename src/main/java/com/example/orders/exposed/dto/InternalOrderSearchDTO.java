package com.example.orders.exposed.dto;

import java.time.LocalDateTime;

public record InternalOrderSearchDTO(
        String externalId,
        String state,
        int page,
        int size,
        LocalDateTime orderDateFrom,
        LocalDateTime orderDateTo,
        String sortBy) {

  public InternalOrderSearchDTO {
    if (page < 0) {
      throw new IllegalArgumentException("page cannot be negative");
    }
    if (size <= 0) {
      throw new IllegalArgumentException("size must be greater than 0");
    }
  }
}
