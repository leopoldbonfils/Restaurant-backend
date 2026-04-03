package com.restaurant.Restaurant_Backend.websocket;

import java.time.LocalDateTime;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.restaurant.Restaurant_Backend.model.Order;
import com.restaurant.Restaurant_Backend.model.OrderStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Broadcasts real-time order-status events to WebSocket subscribers.
 *
 * Two channels:
 *  • /topic/orders              – consumed by the Kitchen Dashboard
 *  • /topic/orders/{table}      – consumed by the Customer's tracking screen
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Broadcast a status change to both the kitchen topic and the
     * table-specific topic so the customer receives it instantly.
     */
    public void notifyOrderStatusChanged(Order order, OrderStatus previousStatus) {

        OrderNotificationEvent event = OrderNotificationEvent.builder()
                .orderId(order.getId())
                .tableNumber(order.getTableNumber())
                .previousStatus(previousStatus)
                .newStatus(order.getStatus())
                .message(buildMessage(order))
                .timestamp(LocalDateTime.now())
                .build();

        messagingTemplate.convertAndSend("/topic/orders", event);

        String tableDestination = "/topic/orders/" + order.getTableNumber();
        messagingTemplate.convertAndSend(tableDestination, event);

        log.debug("WebSocket event sent → orderId={} table={} status={}→{}",
                order.getId(), order.getTableNumber(), previousStatus, order.getStatus());
    }

    /** New order just placed – notify kitchen. */
    public void notifyNewOrder(Order order) {
        notifyOrderStatusChanged(order, null);
    }

    // ── Private helpers ────────────────────────────────────────────────────

    private String buildMessage(Order order) {
        return switch (order.getStatus()) {
            case PENDING    -> "New order from " + order.getTableNumber() + " received!";
            case PREPARING  -> "Order #" + order.getId() + " is now being prepared.";
            case READY      -> "🎉 Order #" + order.getId() + " is ready for delivery!";
            case DELIVERED  -> "Order #" + order.getId() + " has been delivered.";
            case PAID       -> "💰 Order #" + order.getId() + " payment confirmed. Thank you!";
            case CANCELLED  -> "Order #" + order.getId() + " has been cancelled.";
        };
    }
}