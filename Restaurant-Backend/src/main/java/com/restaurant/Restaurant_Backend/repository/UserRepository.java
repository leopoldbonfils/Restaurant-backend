package com.restaurant.Restaurant_Backend.repository;

import com.restaurant.Restaurant_Backend.model.Role;
import com.restaurant.Restaurant_Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data access layer for the User entity.
 *
 * Spring Data JPA generates all SQL automatically —
 * no @Query annotations needed for these simple lookups.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Used by UserDetailsServiceImpl to load a user at login time.
     * Email is the login username in this system.
     */
    Optional<User> findByEmail(String email);

    /**
     * Used during registration to prevent duplicate accounts.
     */
    boolean existsByEmail(String email);

    /**
     * Admin use case: list all kitchen staff or all customers.
     */
    List<User> findByRole(Role role);
}