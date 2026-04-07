package com.restaurant.Restaurant_Backend.dto.response;

import com.restaurant.Restaurant_Backend.model.Role;

/**
 * Returned by both /api/auth/login and /api/auth/register.
 *
 * The frontend stores the token and sends it on every
 * subsequent request as:  Authorization: Bearer <token>
 */
public class AuthResponse {

    private String token;
    private String email;
    private String fullName;
    private Role role;
    private long expiresInMs;

    // ── Constructors ─────────────────────────────────────────────────────────
    public AuthResponse() {}

    public AuthResponse(String token, String email, String fullName,
                        Role role, long expiresInMs) {
        this.token = token;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.expiresInMs = expiresInMs;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public long getExpiresInMs() { return expiresInMs; }
    public void setExpiresInMs(long expiresInMs) { this.expiresInMs = expiresInMs; }
}