package com.restaurant.Restaurant_Backend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

/** POST /api/orders – customer places a new order. */
public class PlaceOrderRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Table number is required")
    private String tableNumber;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemRequest> items;

    private String specialRequests;

    // ── Constructors ──────────────────────────────────────────────────────────

    public PlaceOrderRequest() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getCustomerId()                             { return customerId; }
    public void setCustomerId(Long customerId)              { this.customerId = customerId; }

    public String getTableNumber()                          { return tableNumber; }
    public void   setTableNumber(String tableNumber)        { this.tableNumber = tableNumber; }

    public List<OrderItemRequest> getItems()                { return items; }
    public void setItems(List<OrderItemRequest> items)      { this.items = items; }

    public String getSpecialRequests()                      { return specialRequests; }
    public void   setSpecialRequests(String specialRequests){ this.specialRequests = specialRequests; }

    // ── Nested DTO ────────────────────────────────────────────────────────────

    public static class OrderItemRequest {

        @NotNull(message = "Menu item ID is required")
        private Long menuItemId;

        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        private String itemNotes;

        // ── Constructors ──────────────────────────────────────────────────────

        public OrderItemRequest() {}

        // ── Getters & Setters ─────────────────────────────────────────────────

        public Long getMenuItemId()                         { return menuItemId; }
        public void setMenuItemId(Long menuItemId)          { this.menuItemId = menuItemId; }

        public Integer getQuantity()                        { return quantity; }
        public void    setQuantity(Integer quantity)        { this.quantity = quantity; }

        public String getItemNotes()                        { return itemNotes; }
        public void   setItemNotes(String itemNotes)        { this.itemNotes = itemNotes; }
    }
}