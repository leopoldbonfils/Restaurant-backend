package com.restaurant.Restaurant_Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "table_number", nullable = false, length = 20)
    private String tableNumber;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "loyalty_points", nullable = false)
    private Integer loyaltyPoints = 0;

    @Column(name = "preferred_language", length = 5)
    private String preferredLanguage = "en";

    @Column(name = "checked_in_at", nullable = false, updatable = false)
    private LocalDateTime checkedInAt;

    @Column(name = "checked_out_at")
    private LocalDateTime checkedOutAt;

    @OneToMany(mappedBy = "customer",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    // ── Constructors ─────────────────────────────────────────────────────────

    public Customer() {}

    public Customer(Long id, String tableNumber, String name, String phone,
                    Integer loyaltyPoints, String preferredLanguage,
                    LocalDateTime checkedInAt, LocalDateTime checkedOutAt,
                    List<Order> orders) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.name = name;
        this.phone = phone;
        this.loyaltyPoints = loyaltyPoints != null ? loyaltyPoints : 0;
        this.preferredLanguage = preferredLanguage != null ? preferredLanguage : "en";
        this.checkedInAt = checkedInAt;
        this.checkedOutAt = checkedOutAt;
        this.orders = orders != null ? orders : new ArrayList<>();
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @PrePersist
    public void onCheckIn() {
        this.checkedInAt = LocalDateTime.now();
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public Long getId()                    { return id; }
    public String getTableNumber()         { return tableNumber; }
    public String getName()                { return name; }
    public String getPhone()               { return phone; }
    public Integer getLoyaltyPoints()      { return loyaltyPoints; }
    public String getPreferredLanguage()   { return preferredLanguage; }
    public LocalDateTime getCheckedInAt()  { return checkedInAt; }
    public LocalDateTime getCheckedOutAt() { return checkedOutAt; }
    public List<Order> getOrders()         { return orders; }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setId(Long id)                               { this.id = id; }
    public void setTableNumber(String tableNumber)           { this.tableNumber = tableNumber; }
    public void setName(String name)                         { this.name = name; }
    public void setPhone(String phone)                       { this.phone = phone; }
    public void setLoyaltyPoints(Integer loyaltyPoints)      { this.loyaltyPoints = loyaltyPoints; }
    public void setPreferredLanguage(String preferredLanguage){ this.preferredLanguage = preferredLanguage; }
    public void setCheckedInAt(LocalDateTime checkedInAt)    { this.checkedInAt = checkedInAt; }
    public void setCheckedOutAt(LocalDateTime checkedOutAt)  { this.checkedOutAt = checkedOutAt; }
    public void setOrders(List<Order> orders)                { this.orders = orders; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String tableNumber;
        private String name;
        private String phone;
        private Integer loyaltyPoints = 0;
        private String preferredLanguage = "en";
        private LocalDateTime checkedInAt;
        private LocalDateTime checkedOutAt;
        private List<Order> orders = new ArrayList<>();

        public Builder id(Long id)                              { this.id = id; return this; }
        public Builder tableNumber(String v)                    { this.tableNumber = v; return this; }
        public Builder name(String v)                           { this.name = v; return this; }
        public Builder phone(String v)                          { this.phone = v; return this; }
        public Builder loyaltyPoints(Integer v)                 { this.loyaltyPoints = v; return this; }
        public Builder preferredLanguage(String v)              { this.preferredLanguage = v; return this; }
        public Builder checkedInAt(LocalDateTime v)             { this.checkedInAt = v; return this; }
        public Builder checkedOutAt(LocalDateTime v)            { this.checkedOutAt = v; return this; }
        public Builder orders(List<Order> v)                    { this.orders = v; return this; }

        public Customer build() {
            Customer c = new Customer();
            c.id = this.id;
            c.tableNumber = this.tableNumber;
            c.name = this.name;
            c.phone = this.phone;
            c.loyaltyPoints = this.loyaltyPoints;
            c.preferredLanguage = this.preferredLanguage;
            c.checkedInAt = this.checkedInAt;
            c.checkedOutAt = this.checkedOutAt;
            c.orders = this.orders;
            return c;
        }
    }
}