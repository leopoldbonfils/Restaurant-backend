package com.restaurant.Restaurant_Backend.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The central domain object — one order placed by a customer.
 *
 * Table created automatically: "orders"
 *
 * Columns:
 *   id, customer_id (FK), table_number, status,
 *   total_amount, special_requests,
 *   estimated_prep_minutes,
 *   created_at, updated_at, completed_at
 *
 * Status lifecycle:
 *   PENDING → PREPARING → READY → DELIVERED → PAID
 *                                            ↘ CANCELLED
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The customer who placed this order.
     * FK column: customer_id in the "orders" table.
     * LAZY — only loaded when explicitly accessed.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /**
     * Denormalised copy of the table number.
     * Lets the kitchen see the destination without joining to Customer.
     */
    @NotBlank
    @Column(name = "table_number", nullable = false, length = 20)
    private String tableNumber;

    /**
     * Current stage of the order.
     * Stored as VARCHAR so the kitchen/waiter screens can filter by name.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    /**
     * The line items that make up this order.
     * CascadeType.ALL — saving/deleting Order cascades to its OrderItems.
     * orphanRemoval   — removing an item from the list deletes it from DB.
     */
    @OneToMany(
        mappedBy = "order",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    /**
     * Total price — sum of (unitPrice × quantity) for all items.
     * Recalculated by recalculateTotal() before every save.
     */
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /**
     * Customer's free-text instructions:
     * "No onions", "Extra spicy", "Allergy: nuts", etc.
     */
    @Column(name = "special_requests", length = 500)
    private String specialRequests;

    /**
     * Estimated wait time shown to the customer after placing the order.
     * Calculated as: max(prepTime of all items) + 5 minutes buffer.
     */
    @Column(name = "estimated_prep_minutes")
    private Integer estimatedPrepMinutes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Set when the order reaches PAID or CANCELLED status.
     * Used for analytics (how long did it take to complete an order?).
     */
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // ── Lifecycle hooks ──────────────────────────────────────────────────────

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

    // ── Helper methods ───────────────────────────────────────────────────────

    /**
     * Recalculates totalAmount from the current list of OrderItems.
     * Call this whenever items are added or removed before saving.
     */
    public void recalculateTotal() {
        this.totalAmount = items.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Convenience — add an item and keep total in sync. */
    public void addItem(OrderItem item) {
        item.setOrder(this);
        this.items.add(item);
        recalculateTotal();
    }
}
