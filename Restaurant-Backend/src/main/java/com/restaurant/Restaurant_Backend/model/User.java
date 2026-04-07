package com.restaurant.Restaurant_Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

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
 * NOTE: Explicit getters and setters are written out manually here
 * to avoid NetBeans + Lombok compatibility issues.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.CUSTOMER;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ── Constructors ─────────────────────────────────────────────────────────

    public User() {}

    public User(Long id, String email, String password, Role role,
                String fullName, Boolean isEnabled, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.isEnabled = isEnabled;
        this.createdAt = createdAt;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public Long getId() { return id; }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public Role getRole() { return role; }

    public String getFullName() { return fullName; }

    public Boolean getIsEnabled() { return isEnabled; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    // ── Setters ──────────────────────────────────────────────────────────────

    public void setId(Long id) { this.id = id; }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public void setRole(Role role) { this.role = role; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // ── Builder ──────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String email;
        private String password;
        private Role role = Role.CUSTOMER;
        private String fullName;
        private Boolean isEnabled = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder role(Role role) { this.role = role; return this; }
        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder isEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; return this; }

        public User build() {
            User user = new User();
            user.id = this.id;
            user.email = this.email;
            user.password = this.password;
            user.role = this.role;
            user.fullName = this.fullName;
            user.isEnabled = this.isEnabled;
            return user;
        }
    }

    // ── Lifecycle hook ───────────────────────────────────────────────────────

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}