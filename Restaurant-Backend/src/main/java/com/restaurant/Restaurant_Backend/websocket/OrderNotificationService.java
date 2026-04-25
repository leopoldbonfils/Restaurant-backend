package com.restaurant.Restaurant_Backend.websocket;

import com.restaurant.Restaurant_Backend.model.Order;
import com.restaurant.Restaurant_Backend.model.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Broadcasts real-time order-status events to WebSocket subscribers.
 *
 * Two channels:
 *   /topic/orders          – consumed by the Kitchen Dashboard
 *   /topic/orders/{table}  – consumed by the Customer's tracking screen
 *
 * NOTE: Lombok annotations removed for NetBeans compatibility.
 */
@Service
public class OrderNotificationService {

    private static final Logger log = LoggerFactory.getLogger(OrderNotificationService.class);

    private final SimpMessagingTemplate messagingTemplate;

    public OrderNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

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

        log.debug("WebSocket event → orderId={} table={} {}→{}",
                order.getId(), order.getTableNumber(), previousStatus, order.getStatus());
    }

    public void notifyNewOrder(Order order) {
        notifyOrderStatusChanged(order, null);
    }

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