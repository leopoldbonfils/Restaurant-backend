package com.restaurant.Restaurant_Backend.dto.request;

import com.restaurant.Restaurant_Backend.model.OrderStatus;
import jakarta.validation.constraints.NotNull;

/** PATCH /api/orders/{id}/status – kitchen or waiter updates status. */
public class UpdateOrderStatusRequest {

    @NotNull(message = "New status is required")
    private OrderStatus status;

    // ── Constructors ──────────────────────────────────────────────────────────

    public UpdateOrderStatusRequest() {}

    public UpdateOrderStatusRequest(OrderStatus status) {
        this.status = status;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public OrderStatus getStatus()              { return status; }
    public void        setStatus(OrderStatus s) { this.status = s; }
}