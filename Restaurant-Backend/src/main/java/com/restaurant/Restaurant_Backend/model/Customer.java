package com.restaurant.Restaurant_Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identifies which physical table the customer is sitting at.
     * e.g. "Table 5", "T-12"
     */
    @NotBlank
    @Column(name = "table_number", nullable = false, length = 20)
    private String tableNumber;

    /** Optional — collected during check-in or loyalty sign-up. */
    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * Loyalty points earned: 1 point per 100 RWF spent.
     * Default 0 — never null.
     */
    @Column(name = "loyalty_points", nullable = false)
    @Builder.Default
    private Integer loyaltyPoints = 0;

    /**
     * ISO 639-1 language code: "en", "fr", "sw".
     * Drives the front-end language selection.
     */
    @Column(name = "preferred_language", length = 5)
    @Builder.Default
    private String preferredLanguage = "en";

    /** Timestamp when the customer scanned the QR code and checked in. */
    @Column(name = "checked_in_at", nullable = false, updatable = false)
    private LocalDateTime checkedInAt;

    /**
     * Null while the session is active.
     * Set when the waiter confirms payment and closes the table.
     */
    @Column(name = "checked_out_at")
    private LocalDateTime checkedOutAt;

    /**
     * All orders placed by this customer during this session.
     * mappedBy = "customer" links to Order.customer field.
     * CascadeType.ALL: saving Customer also saves its Orders.
     */
    @OneToMany(mappedBy = "customer",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY)
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    // ── Lifecycle hooks ──────────────────────────────────────────────────────

    @PrePersist
    public void onCheckIn() {
        this.checkedInAt = LocalDateTime.now();
    }
}