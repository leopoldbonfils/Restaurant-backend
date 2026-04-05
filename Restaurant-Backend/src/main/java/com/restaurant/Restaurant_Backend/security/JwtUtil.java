package com.restaurant.Restaurant_Backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Handles all JWT operations:
 *   - Generating a token after successful login
 *   - Extracting the email (subject) from a token
 *   - Validating a token's signature and expiry
 *
 * The secret and expiry are read from application.properties:
 *   app.jwt.secret
 *   app.jwt.expiration-ms
 */
@Component
public class JwtUtil {

    // Manual logger declaration — avoids NetBeans + Lombok @Slf4j compatibility issue
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    // ── Token generation ─────────────────────────────────────────────────────

    /**
     * Creates a signed JWT token for the given user.
     * The token subject is the user's email.
     * Expiry is set based on app.jwt.expiration-ms (default 24 hours).
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())   // email
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ── Token reading ────────────────────────────────────────────────────────

    /**
     * Extracts the email (subject) from a token.
     * Call this only after validateToken() returns true.
     */
    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    // ── Token validation ─────────────────────────────────────────────────────

    /**
     * Returns true if the token:
     *   1. Has a valid signature (matches our secret)
     *   2. Has not expired
     *   3. Belongs to the given UserDetails (email matches)
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String email = extractEmail(token);
            return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Lightweight check — validates signature and expiry only.
     * Used by JwtAuthenticationFilter before loading UserDetails.
     */
    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT unsupported: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("JWT malformed: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT empty or null: {}", e.getMessage());
        }
        return false;
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private boolean isTokenExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Decodes the base64 secret from application.properties
     * and builds an HMAC-SHA key from it.
     *
     * IMPORTANT: your secret must be at least 256 bits (32 bytes).
     * Generate one with: openssl rand -hex 64
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}