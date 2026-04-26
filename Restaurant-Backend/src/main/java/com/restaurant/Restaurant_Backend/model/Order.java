package com.restaurant.Restaurant_Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotBlank
    @Column(name = "table_number", nullable = false, length = 20)
    private String tableNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.PENDING;

    @OneToMany(
        mappedBy = "order",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<OrderItem> items = new ArrayList<>();

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "special_requests", length = 500)
    private String specialRequests;

    @Column(name = "estimated_prep_minutes")
    private Integer estimatedPrepMinutes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Order() {}

    // ── Lifecycle hooks ───────────────────────────────────────────────────────

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (this.status == OrderStatus.PAID
                || this.status == OrderStatus.CANCELLED) {
            this.completedAt = LocalDateTime.now();
        }
    }

    // ── Helper methods ────────────────────────────────────────────────────────

    public void recalculateTotal() {
        this.totalAmount = items.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addItem(OrderItem item) {
        item.setOrder(this);
        this.items.add(item);
        recalculateTotal();
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public Long getId()                        { return id; }
    public Customer getCustomer()              { return customer; }
    public String getTableNumber()             { return tableNumber; }
    public OrderStatus getStatus()             { return status; }
    public List<OrderItem> getItems()          { return items; }
    public BigDecimal getTotalAmount()         { return totalAmount; }
    public String getSpecialRequests()         { return specialRequests; }
    public Integer getEstimatedPrepMinutes()   { return estimatedPrepMinutes; }
    public LocalDateTime getCreatedAt()        { return createdAt; }
    public LocalDateTime getUpdatedAt()        { return updatedAt; }
    public LocalDateTime getCompletedAt()      { return completedAt; }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setId(Long id)                                   { this.id = id; }
    public void setCustomer(Customer customer)                   { this.customer = customer; }
    public void setTableNumber(String tableNumber)               { this.tableNumber = tableNumber; }
    public void setStatus(OrderStatus status)                    { this.status = status; }
    public void setItems(List<OrderItem> items)                  { this.items = items; }
    public void setTotalAmount(BigDecimal totalAmount)           { this.totalAmount = totalAmount; }
    public void setSpecialRequests(String specialRequests)       { this.specialRequests = specialRequests; }
    public void setEstimatedPrepMinutes(Integer v)               { this.estimatedPrepMinutes = v; }
    public void setCreatedAt(LocalDateTime createdAt)            { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt)            { this.updatedAt = updatedAt; }
    public void setCompletedAt(LocalDateTime completedAt)        { this.completedAt = completedAt; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Customer customer;
        private String tableNumber;
        private OrderStatus status = OrderStatus.PENDING;
        private List<OrderItem> items = new ArrayList<>();
        private BigDecimal totalAmount = BigDecimal.ZERO;
        private String specialRequests;
        private Integer estimatedPrepMinutes;

        public Builder id(Long v)                       { this.id = v; return this; }
        public Builder customer(Customer v)             { this.customer = v; return this; }
        public Builder tableNumber(String v)            { this.tableNumber = v; return this; }
        public Builder status(OrderStatus v)            { this.status = v; return this; }
        public Builder items(List<OrderItem> v)         { this.items = v; return this; }
        public Builder totalAmount(BigDecimal v)        { this.totalAmount = v; return this; }
        public Builder specialRequests(String v)        { this.specialRequests = v; return this; }
        public Builder estimatedPrepMinutes(Integer v)  { this.estimatedPrepMinutes = v; return this; }

        public Order build() {
            Order o = new Order();
            o.id = this.id;
            o.customer = this.customer;
            o.tableNumber = this.tableNumber;
            o.status = this.status;
            o.items = this.items;
            o.totalAmount = this.totalAmount;
            o.specialRequests = this.specialRequests;
            o.estimatedPrepMinutes = this.estimatedPrepMinutes;
            return o;
        }
    }
}