package com.restaurant.Restaurant_Backend.dto.response;

import java.math.BigDecimal;

public class OrderItemResponse {

    private Long id;
    private Long menuItemId;
    private String menuItemName;
    private String menuItemEmoji;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
    private String itemNotes;

    // ── Constructors ──────────────────────────────────────────────────────────

    public OrderItemResponse() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId()                                 { return id; }
    public void setId(Long id)                          { this.id = id; }

    public Long getMenuItemId()                         { return menuItemId; }
    public void setMenuItemId(Long menuItemId)          { this.menuItemId = menuItemId; }

    public String getMenuItemName()                     { return menuItemName; }
    public void   setMenuItemName(String menuItemName)  { this.menuItemName = menuItemName; }

    public String getMenuItemEmoji()                    { return menuItemEmoji; }
    public void   setMenuItemEmoji(String menuItemEmoji){ this.menuItemEmoji = menuItemEmoji; }

    public Integer getQuantity()                        { return quantity; }
    public void    setQuantity(Integer quantity)        { this.quantity = quantity; }

    public BigDecimal getUnitPrice()                    { return unitPrice; }
    public void       setUnitPrice(BigDecimal unitPrice){ this.unitPrice = unitPrice; }

    public BigDecimal getLineTotal()                    { return lineTotal; }
    public void       setLineTotal(BigDecimal lineTotal){ this.lineTotal = lineTotal; }

    public String getItemNotes()                        { return itemNotes; }
    public void   setItemNotes(String itemNotes)        { this.itemNotes = itemNotes; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long menuItemId;
        private String menuItemName;
        private String menuItemEmoji;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal lineTotal;
        private String itemNotes;

        public Builder id(Long v)                    { this.id = v; return this; }
        public Builder menuItemId(Long v)            { this.menuItemId = v; return this; }
        public Builder menuItemName(String v)        { this.menuItemName = v; return this; }
        public Builder menuItemEmoji(String v)       { this.menuItemEmoji = v; return this; }
        public Builder quantity(Integer v)           { this.quantity = v; return this; }
        public Builder unitPrice(BigDecimal v)       { this.unitPrice = v; return this; }
        public Builder lineTotal(BigDecimal v)       { this.lineTotal = v; return this; }
        public Builder itemNotes(String v)           { this.itemNotes = v; return this; }

        public OrderItemResponse build() {
            OrderItemResponse r = new OrderItemResponse();
            r.id = this.id;
            r.menuItemId = this.menuItemId;
            r.menuItemName = this.menuItemName;
            r.menuItemEmoji = this.menuItemEmoji;
            r.quantity = this.quantity;
            r.unitPrice = this.unitPrice;
            r.lineTotal = this.lineTotal;
            r.itemNotes = this.itemNotes;
            return r;
        }
    }
}