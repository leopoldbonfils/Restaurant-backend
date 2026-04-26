package com.restaurant.Restaurant_Backend.dto.request;

import jakarta.validation.constraints.NotBlank;

/** POST /api/customers – check a customer into a table. */
public class CustomerCheckInRequest {

    @NotBlank(message = "Table number is required")
    private String tableNumber;

    private String name;
    private String phone;
    private String preferredLanguage = "en";

    // ── Constructors ──────────────────────────────────────────────────────────

    public CustomerCheckInRequest() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getTableNumber()                          { return tableNumber; }
    public void   setTableNumber(String tableNumber)        { this.tableNumber = tableNumber; }

    public String getName()                                 { return name; }
    public void   setName(String name)                      { this.name = name; }

    public String getPhone()                                { return phone; }
    public void   setPhone(String phone)                    { this.phone = phone; }

    public String getPreferredLanguage()                    { return preferredLanguage; }
    public void   setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }
}