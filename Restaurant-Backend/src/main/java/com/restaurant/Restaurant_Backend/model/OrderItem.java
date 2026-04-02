package com.restaurant.Restaurant_Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

/**
 * One line in an order — which menu item, how many, at what price.
 *
 * Table created automatically: "order_items"
 *
 * Columns:
 *   id, order_id (FK), menu_item_id (FK),
 *   quantity, unit_price, item_notes
 *
 * KEY DESIGN DECISION — price snapshot:
 *   unitPrice is copied from MenuItem.price at the moment the order is
 *   placed. This means menu price changes never retroactively alter
 *   existing or historical orders. Always charge what was shown.
 */
@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The parent order.
     * FK column: order_id in "order_items" table.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * The menu item being ordered.
     * EAGER — always loaded so we can show name/emoji on the kitchen screen.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer quantity;

    /**
     * Price snapshotted at order time — see design decision above.
     * precision = 10 digits total, scale = 2 decimal places.
     */
    @NotNull
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    /**
     * Per-item customer note: "No onions", "Well done", "Extra sauce".
     * Separate from Order.specialRequests which covers the whole order.
     */
    @Column(name = "item_notes", length = 300)
    private String itemNotes;

    // ── Helper ───────────────────────────────────────────────────────────────

    /**
     * Returns unitPrice × quantity.
     * Used by Order.recalculateTotal() to sum all line totals.
     */
    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}