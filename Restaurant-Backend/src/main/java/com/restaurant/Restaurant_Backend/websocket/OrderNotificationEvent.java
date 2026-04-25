package com.restaurant.Restaurant_Backend.websocket;

import com.restaurant.Restaurant_Backend.model.OrderStatus;
import java.time.LocalDateTime;

/**
 * Payload pushed to WebSocket subscribers whenever an order status changes.
 * Lombok removed for NetBeans compatibility — uses manual builder pattern.
 */
public class OrderNotificationEvent {

    private Long        orderId;
    private String      tableNumber;
    private OrderStatus previousStatus;
    private OrderStatus newStatus;
    private String      message;
    private LocalDateTime timestamp;

    // ── Constructors ─────────────────────────────────────────────────────────
    public OrderNotificationEvent() {}

    // ── Getters ──────────────────────────────────────────────────────────────
    public Long        getOrderId()        { return orderId; }
    public String      getTableNumber()    { return tableNumber; }
    public OrderStatus getPreviousStatus() { return previousStatus; }
    public OrderStatus getNewStatus()      { return newStatus; }
    public String      getMessage()        { return message; }
    public LocalDateTime getTimestamp()    { return timestamp; }

    // ── Setters ──────────────────────────────────────────────────────────────
    public void setOrderId(Long orderId)                  { this.orderId = orderId; }
    public void setTableNumber(String tableNumber)        { this.tableNumber = tableNumber; }
    public void setPreviousStatus(OrderStatus prev)       { this.previousStatus = prev; }
    public void setNewStatus(OrderStatus newStatus)       { this.newStatus = newStatus; }
    public void setMessage(String message)                { this.message = message; }
    public void setTimestamp(LocalDateTime timestamp)     { this.timestamp = timestamp; }

    // ── Builder ───────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final OrderNotificationEvent e = new OrderNotificationEvent();

        public Builder orderId(Long v)             { e.orderId = v;         return this; }
        public Builder tableNumber(String v)       { e.tableNumber = v;     return this; }
        public Builder previousStatus(OrderStatus v){ e.previousStatus = v; return this; }
        public Builder newStatus(OrderStatus v)    { e.newStatus = v;       return this; }
        public Builder message(String v)           { e.message = v;         return this; }
        public Builder timestamp(LocalDateTime v)  { e.timestamp = v;       return this; }

        public OrderNotificationEvent build() { return e; }
    }
}