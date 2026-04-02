package com.restaurant.Restaurant_Backend.dto.request;



import com.restaurant.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** PATCH /api/orders/{id}/status – kitchen or waiter updates status. */
@Data
public class UpdateOrderStatusRequest {

    @NotNull(message = "New status is required")
    private OrderStatus status;
}
