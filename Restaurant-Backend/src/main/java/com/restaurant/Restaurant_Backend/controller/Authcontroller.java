package com.restaurant.Restaurant_Backend.controller;

import com.restaurant.Restaurant_Backend.dto.request.LoginRequest;
import com.restaurant.Restaurant_Backend.dto.request.RegisterRequest;
import com.restaurant.Restaurant_Backend.dto.response.ApiResponse;
import com.restaurant.Restaurant_Backend.dto.response.AuthResponse;
import com.restaurant.Restaurant_Backend.exception.BadRequestException;
import com.restaurant.Restaurant_Backend.model.Role;
import com.restaurant.Restaurant_Backend.model.User;
import com.restaurant.Restaurant_Backend.repository.UserRepository;
import com.restaurant.Restaurant_Backend.security.JwtUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Handles user registration and login.
 *
 * Public endpoints (no token required):
 *   POST /api/auth/register  — create a new account
 *   POST /api/auth/login     — authenticate and receive a JWT token
 *
 * The JWT returned must be sent on all subsequent requests as:
 *   Authorization: Bearer <token>
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    public AuthController(UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtUtil jwtUtil,
        AuthenticationManager authenticationManager,
        UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    // ── Register ─────────────────────────────────────────────────────────────

    /**
     * Creates a new user account and returns a JWT token immediately
     * so the user doesn't have to log in separately after registering.
     *
     * Rules:
     *   - Email must be unique
     *   - Password is BCrypt-hashed before saving
     *   - Default role is CUSTOMER unless specified
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        // Block duplicate email addresses
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(
                "An account with email " + request.getEmail() + " already exists.");
        }

        // Default role to CUSTOMER if not provided
        Role role = request.getRole() != null ? request.getRole() : Role.CUSTOMER;

        // Build and save the new user
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // hash password
                .fullName(request.getFullName())
                .role(role)
                .isEnabled(true)
                .build();

        userRepository.save(user);
        log.info("New user registered: {} with role {}", user.getEmail(), user.getRole());

        // Generate token so frontend can use the app immediately
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        AuthResponse authResponse = new AuthResponse(
                token,
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                jwtExpirationMs
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Account created successfully.", authResponse));
    }

    // ── Login ─────────────────────────────────────────────────────────────────

    /**
     * Authenticates email + password and returns a JWT token.
     *
     * Spring Security's AuthenticationManager handles the actual
     * credential check — it calls UserDetailsServiceImpl.loadUserByUsername()
     * and verifies the BCrypt hash automatically.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        try {
            // Let Spring Security verify email + password
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Invalid email or password.");
        }

        // Credentials are valid — load user and generate token
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        // Load full user details for the response
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found."));

        log.info("User logged in: {}", user.getEmail());

        AuthResponse authResponse = new AuthResponse(
                token,
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                jwtExpirationMs
        );

        return ResponseEntity.ok(
                ApiResponse.ok("Login successful.", authResponse));
    }
}