package com.restaurant.Restaurant_Backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Central security configuration.
 *
 * Key decisions:
 *   - Stateless sessions (JWT — no server-side session storage)
 *   - CSRF disabled (safe for stateless REST APIs)
 *   - BCrypt password hashing
 *   - JwtAuthenticationFilter runs before Spring's default login filter
 *   - Endpoint rules follow least-privilege: default DENY, explicit PERMIT
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity   // enables @PreAuthorize on controller methods
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // ── Password encoder ─────────────────────────────────────────────────────

    /**
     * BCrypt is the industry standard for password hashing.
     * Spring Security uses this bean automatically during authentication.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ── Authentication provider ──────────────────────────────────────────────

    /**
     * Wires our UserDetailsService + PasswordEncoder together.
     * Spring Security calls this during login to validate credentials.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // ── Authentication manager ───────────────────────────────────────────────

    /**
     * Exposes the AuthenticationManager as a bean so AuthController
     * can call authenticate() during login.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ── Security filter chain ────────────────────────────────────────────────

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF — not needed for stateless JWT APIs
            .csrf(AbstractHttpConfigurer::disable)

            // Stateless — no HttpSession created or used
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Endpoint access rules
            .authorizeHttpRequests(auth -> auth

                // ── Fully public endpoints ───────────────────────────────
                // Auth: anyone can register or login
                .requestMatchers("/api/auth/**").permitAll()

                // WebSocket handshake endpoint
                .requestMatchers("/ws/**").permitAll()

                // Customer-facing menu (read-only, no login required)
                .requestMatchers(HttpMethod.GET, "/api/menu-items").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/menu-items/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/menu-items/category/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/menu-items/categories").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/menu-items/dietary/**").permitAll()

                // ── Customer endpoints (must be logged in) ───────────────
                .requestMatchers("/api/customers/**").hasAnyRole("CUSTOMER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/orders").hasAnyRole("CUSTOMER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/orders/table/**").hasAnyRole("CUSTOMER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/orders/customer/**").hasAnyRole("CUSTOMER", "ADMIN")

                // ── Kitchen endpoints ────────────────────────────────────
                .requestMatchers(HttpMethod.GET, "/api/orders/active").hasAnyRole("KITCHEN", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/orders/*/status").hasAnyRole("KITCHEN", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/orders/*/cancel").hasAnyRole("KITCHEN", "ADMIN")

                // ── Admin-only endpoints ─────────────────────────────────
                .requestMatchers(HttpMethod.GET, "/api/orders").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/orders/analytics").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/menu-items/all").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/menu-items").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/menu-items/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/menu-items/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/menu-items/*/toggle-availability").hasRole("ADMIN")

                // Everything else requires authentication
                .anyRequest().authenticated()
            )

            // Wire our JWT filter before Spring's default username/password filter
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}