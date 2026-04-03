package com.restaurant.Restaurant_Backend.repository;


import com.restaurant.Restaurant_Backend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findTopByTableNumberOrderByCheckedInAtDesc(String tableNumber);

    /** All active sessions (not yet checked out) for a given table. */
    List<Customer> findByTableNumberAndCheckedOutAtIsNull(String tableNumber);

    boolean existsByTableNumberAndCheckedOutAtIsNull(String tableNumber);
}