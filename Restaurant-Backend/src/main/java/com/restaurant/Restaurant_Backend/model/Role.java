package com.restaurant.Restaurant_Backend.model;

/**
 * The three user roles in the system.
 *
 * CUSTOMER  – scans QR code, browses menu, places & tracks orders
 * KITCHEN   – views the kitchen board, advances order status
 * ADMIN     – full access: menu management, analytics, staff oversight
 *
 * Stored as a VARCHAR in the "users" table via @Enumerated(EnumType.STRING).
 * Spring Security expects role names prefixed with "ROLE_" — that prefix is
 * added in UserDetailsServiceImpl, not here, so this enum stays clean.
 */
public enum Role {
    CUSTOMER,
    KITCHEN,
    ADMIN
}