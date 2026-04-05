package com.restaurant.Restaurant_Backend.security;

import com.restaurant.Restaurant_Backend.model.User;
import com.restaurant.Restaurant_Backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Tells Spring Security how to load a user by their email (username).
 *
 * Spring Security calls loadUserByUsername() in two places:
 *   1. During login — AuthController asks AuthenticationManager to
 *      authenticate credentials, which internally calls this method.
 *   2. During JWT filter — JwtAuthenticationFilter calls this after
 *      extracting the email from a valid token.
 *
 * The returned UserDetails object is what Spring Security uses to:
 *   - Check the password at login
 *   - Check roles/authorities for @PreAuthorize and SecurityConfig rules
 *   - Check isEnabled / isAccountNonLocked before granting access
 */
@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Loading user by email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No user found with email: " + email));

        /*
         * Role → GrantedAuthority mapping.
         *
         * Spring Security expects authorities prefixed with "ROLE_".
         * So Role.ADMIN becomes "ROLE_ADMIN", Role.KITCHEN becomes
         * "ROLE_KITCHEN", etc.
         *
         * This prefix lets us use:
         *   - hasRole("ADMIN")       in SecurityConfig
         *   - @PreAuthorize("hasRole('KITCHEN')") on controller methods
         */
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        /*
         * We return Spring's built-in User object (not our User entity).
         * It takes: username, password, enabled, accountNonExpired,
         *           credentialsNonExpired, accountNonLocked, authorities
         *
         * We use isEnabled from our User entity to allow admins to
         * disable accounts without deleting them.
         */
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())        // BCrypt hash from DB
                .authorities(List.of(authority))     // ROLE_ADMIN / ROLE_KITCHEN / ROLE_CUSTOMER
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.getIsEnabled())      // blocks disabled accounts
                .build();
    }
}