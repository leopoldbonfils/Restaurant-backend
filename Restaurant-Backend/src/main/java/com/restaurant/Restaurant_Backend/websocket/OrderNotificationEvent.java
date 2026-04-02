package com.restaurant.Restaurant_Backend.websocket;


import com.restaurant.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Payload pushed to WebSocket subscribers whenever an order status changes.
 *
 * Clients subscribe to:
 *   /topic/orders               – kitchen board (all active orders)
 *   /topic/orders/{tableNumber} – customer's own tracking screen
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderNotificationEvent {

    private Long orderId;
    private String tableNumber;
    private OrderStatus previousStatus;
    private OrderStatus newStatus;
    private String message;           // human-readable, used for toasts
    private LocalDateTime timestamp;
}
