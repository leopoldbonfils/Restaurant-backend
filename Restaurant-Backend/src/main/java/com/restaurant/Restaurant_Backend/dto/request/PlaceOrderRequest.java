package com.restaurant.Restaurant_Backend.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

/** POST /api/orders – customer places a new order. */
@Data
public class PlaceOrderRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Table number is required")
    private String tableNumber;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemRequest> items;

    private String specialRequests;

    // ── Nested DTO ─────────────────────────────────────────────────────────
    @Data
    public static class OrderItemRequest {

        @NotNull(message = "Menu item ID is required")
        private Long menuItemId;

        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        private String itemNotes;   // per-item custom note
    }
}
