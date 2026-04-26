package com.restaurant.Restaurant_Backend.dto.response;

import com.restaurant.Restaurant_Backend.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long id;
    private Long customerId;
    private String tableNumber;
    private OrderStatus status;
    private List<OrderItemResponse> items;
    private BigDecimal totalAmount;
    private String specialRequests;
    private Integer estimatedPrepMinutes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;

    // ── Constructors ──────────────────────────────────────────────────────────

    public OrderResponse() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId()                                         { return id; }
    public void setId(Long id)                                  { this.id = id; }

    public Long getCustomerId()                                 { return customerId; }
    public void setCustomerId(Long customerId)                  { this.customerId = customerId; }

    public String getTableNumber()                              { return tableNumber; }
    public void   setTableNumber(String tableNumber)            { this.tableNumber = tableNumber; }

    public OrderStatus getStatus()                              { return status; }
    public void        setStatus(OrderStatus status)            { this.status = status; }

    public List<OrderItemResponse> getItems()                   { return items; }
    public void setItems(List<OrderItemResponse> items)         { this.items = items; }

    public BigDecimal getTotalAmount()                          { return totalAmount; }
    public void       setTotalAmount(BigDecimal totalAmount)    { this.totalAmount = totalAmount; }

    public String getSpecialRequests()                          { return specialRequests; }
    public void   setSpecialRequests(String specialRequests)    { this.specialRequests = specialRequests; }

    public Integer getEstimatedPrepMinutes()                    { return estimatedPrepMinutes; }
    public void    setEstimatedPrepMinutes(Integer v)           { this.estimatedPrepMinutes = v; }

    public LocalDateTime getCreatedAt()                         { return createdAt; }
    public void          setCreatedAt(LocalDateTime createdAt)  { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt()                         { return updatedAt; }
    public void          setUpdatedAt(LocalDateTime updatedAt)  { this.updatedAt = updatedAt; }

    public LocalDateTime getCompletedAt()                       { return completedAt; }
    public void          setCompletedAt(LocalDateTime v)        { this.completedAt = v; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long customerId;
        private String tableNumber;
        private OrderStatus status;
        private List<OrderItemResponse> items;
        private BigDecimal totalAmount;
        private String specialRequests;
        private Integer estimatedPrepMinutes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime completedAt;

        public Builder id(Long v)                            { this.id = v; return this; }
        public Builder customerId(Long v)                    { this.customerId = v; return this; }
        public Builder tableNumber(String v)                 { this.tableNumber = v; return this; }
        public Builder status(OrderStatus v)                 { this.status = v; return this; }
        public Builder items(List<OrderItemResponse> v)      { this.items = v; return this; }
        public Builder totalAmount(BigDecimal v)             { this.totalAmount = v; return this; }
        public Builder specialRequests(String v)             { this.specialRequests = v; return this; }
        public Builder estimatedPrepMinutes(Integer v)       { this.estimatedPrepMinutes = v; return this; }
        public Builder createdAt(LocalDateTime v)            { this.createdAt = v; return this; }
        public Builder updatedAt(LocalDateTime v)            { this.updatedAt = v; return this; }
        public Builder completedAt(LocalDateTime v)          { this.completedAt = v; return this; }

        public OrderResponse build() {
            OrderResponse r = new OrderResponse();
            r.id = this.id;
            r.customerId = this.customerId;
            r.tableNumber = this.tableNumber;
            r.status = this.status;
            r.items = this.items;
            r.totalAmount = this.totalAmount;
            r.specialRequests = this.specialRequests;
            r.estimatedPrepMinutes = this.estimatedPrepMinutes;
            r.createdAt = this.createdAt;
            r.updatedAt = this.updatedAt;
            r.completedAt = this.completedAt;
            return r;
        }
    }
}