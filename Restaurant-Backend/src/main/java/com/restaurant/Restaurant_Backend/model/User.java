package com.restaurant.Restaurant_Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a system user who can log in.
 *
 * Three types map to the three roles:
 *   - Customers  (CUSTOMER) — created automatically on QR check-in
 *   - Kitchen staff (KITCHEN) — created by Admin
 *   - Admins    (ADMIN)   — created manually or seeded
 *
 * Table: "users"
 *
 * Columns:
 *   id, email, password, role, full_name,
 *   is_enabled, created_at
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Used as the login username.
     * Must be unique across all users.
     */
    @Email
    @NotBlank
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /**
     * BCrypt-hashed password.
     * Never stored as plain text.
     */
    @NotBlank
    @Column(nullable = false)
    private String password;

    /**
     * Determines what the user can access.
     * Stored as a string e.g. "ADMIN", "KITCHEN", "CUSTOMER".
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Role role = Role.CUSTOMER;

    /**
     * Display name shown on kitchen and admin screens.
     * Optional — customers may not have one.
     */
    @Column(name = "full_name", length = 100)
    private String fullName;

    /**
     * Admin can disable a user account without deleting it.
     * Disabled users cannot log in.
     */
    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    private Boolean isEnabled = true;

    /**
     * Timestamp of account creation.
     * Set automatically by @PrePersist — never updated after that.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ── Lifecycle hook ───────────────────────────────────────────────────────

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}