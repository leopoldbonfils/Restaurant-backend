package com.restaurant.Restaurant_Backend.dto.request;

import com.restaurant.Restaurant_Backend.model.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** POST /api/auth/register */
public class RegisterRequest {

    @Email(message = "Must be a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    /** Optional display name shown on kitchen/admin screens. */
    private String fullName;

    /**
     * Role to assign. Defaults to CUSTOMER if not provided.
     * Only ADMIN can register KITCHEN or ADMIN accounts —
     * that rule is enforced in AuthController, not here.
     */
    private Role role = Role.CUSTOMER;

    // ── Constructors ─────────────────────────────────────────────────────────
    public RegisterRequest() {}

    public RegisterRequest(String email, String password, String fullName, Role role) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}