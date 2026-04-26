package com.restaurant.Restaurant_Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "item_notes", length = 300)
    private String itemNotes;

    // ── Constructors ──────────────────────────────────────────────────────────

    public OrderItem() {}

    public OrderItem(Long id, Order order, MenuItem menuItem,
                     Integer quantity, BigDecimal unitPrice, String itemNotes) {
        this.id = id;
        this.order = order;
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.itemNotes = itemNotes;
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public Long getId()             { return id; }
    public Order getOrder()         { return order; }
    public MenuItem getMenuItem()   { return menuItem; }
    public Integer getQuantity()    { return quantity; }
    public BigDecimal getUnitPrice(){ return unitPrice; }
    public String getItemNotes()    { return itemNotes; }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setId(Long id)                   { this.id = id; }
    public void setOrder(Order order)            { this.order = order; }
    public void setMenuItem(MenuItem menuItem)   { this.menuItem = menuItem; }
    public void setQuantity(Integer quantity)    { this.quantity = quantity; }
    public void setUnitPrice(BigDecimal unitPrice){ this.unitPrice = unitPrice; }
    public void setItemNotes(String itemNotes)   { this.itemNotes = itemNotes; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Order order;
        private MenuItem menuItem;
        private Integer quantity;
        private BigDecimal unitPrice;
        private String itemNotes;

        public Builder id(Long v)               { this.id = v;         return this; }
        public Builder order(Order v)           { this.order = v;      return this; }
        public Builder menuItem(MenuItem v)     { this.menuItem = v;   return this; }
        public Builder quantity(Integer v)      { this.quantity = v;   return this; }
        public Builder unitPrice(BigDecimal v)  { this.unitPrice = v;  return this; }
        public Builder itemNotes(String v)      { this.itemNotes = v;  return this; }

        public OrderItem build() {
            OrderItem i = new OrderItem();
            i.id = this.id;
            i.order = this.order;
            i.menuItem = this.menuItem;
            i.quantity = this.quantity;
            i.unitPrice = this.unitPrice;
            i.itemNotes = this.itemNotes;
            return i;
        }
    }
}