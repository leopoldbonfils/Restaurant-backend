package com.restaurant.Restaurant_Backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Runs once per HTTP request — before the request reaches any controller.
 *
 * What it does:
 *   1. Looks for an Authorization header in the format: "Bearer <token>"
 *   2. If found, validates the token using JwtUtil
 *   3. If valid, loads the user from DB and sets the authentication
 *      in Spring's SecurityContext so the request is treated as authenticated
 *   4. If no token or invalid token — does nothing, lets the request through
 *      (SecurityConfig will then block it if the endpoint requires auth)
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Step 1: Extract the token from the Authorization header
        String token = extractToken(request);

        // Step 2: If there's a token and it looks structurally valid
        if (token != null && jwtUtil.isTokenValid(token)) {

            // Step 3: Extract the email from the token
            String email = jwtUtil.extractEmail(token);

            // Step 4: Only proceed if no authentication is already set
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Step 5: Load the full user from DB
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // Step 6: Double-check token is valid for this specific user
                if (jwtUtil.validateToken(token, userDetails)) {

                    // Step 7: Create an authentication object and put it in the SecurityContext
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,                        // no credentials needed after token validation
                                    userDetails.getAuthorities() // roles: ROLE_ADMIN, ROLE_KITCHEN, etc.
                            );
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Authenticated user: {} | URI: {}", email, request.getRequestURI());
                }
            }
        }

        // Step 8: Always continue the filter chain — never block here
        filterChain.doFilter(request, response);
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    /**
     * Extracts the JWT from the Authorization header.
     * Expected format: "Bearer eyJhbGci..."
     * Returns null if the header is missing or malformed.
     */
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7); // remove "Bearer " prefix
        }
        return null;
    }
} 
    

