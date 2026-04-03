package com.restaurant.Restaurant_Backend.service;

import java.util.List;

import com.restaurant.Restaurant_Backend.dto.request.CustomerCheckInRequest;
import com.restaurant.Restaurant_Backend.dto.response.CustomerResponse;

public interface CustomerService {

    /** Check a new customer into a table – creates a Customer session. */
    CustomerResponse checkIn(CustomerCheckInRequest request);

    /** Check out – marks the session as ended, freezes loyalty points. */
    CustomerResponse checkOut(Long customerId);

    CustomerResponse findById(Long id);

    /** Returns the currently active session for a table number. */
    CustomerResponse findActiveByTable(String tableNumber);

    List<CustomerResponse> findAll();

    /** Award loyalty points (called internally after a paid order). */
    void awardLoyaltyPoints(Long customerId, int points);
}