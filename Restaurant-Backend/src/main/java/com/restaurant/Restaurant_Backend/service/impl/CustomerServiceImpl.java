package com.restaurant.Restaurant_Backend.service.impl;

import com.restaurant.Restaurant_Backend.dto.request.CustomerCheckInRequest;
import com.restaurant.Restaurant_Backend.dto.response.CustomerResponse;
import com.restaurant.Restaurant_Backend.exception.BadRequestException;
import com.restaurant.Restaurant_Backend.exception.ResourceNotFoundException;
import com.restaurant.Restaurant_Backend.model.Customer;
import com.restaurant.Restaurant_Backend.repository.CustomerRepository;
import com.restaurant.Restaurant_Backend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse checkIn(CustomerCheckInRequest request) {
        if (customerRepository.existsByTableNumberAndCheckedOutAtIsNull(request.getTableNumber())) {
            throw new BadRequestException(
                "Table " + request.getTableNumber() + " already has an active session.");
        }
        Customer customer = Customer.builder()
                .tableNumber(request.getTableNumber())
                .name(request.getName())
                .phone(request.getPhone())
                .preferredLanguage(
                    request.getPreferredLanguage() != null ? request.getPreferredLanguage() : "en")
                .build();
        return toResponse(customerRepository.save(customer));
    }

    @Override
    public CustomerResponse checkOut(Long customerId) {
        Customer customer = findCustomerById(customerId);
        if (customer.getCheckedOutAt() != null) {
            throw new BadRequestException("Customer already checked out.");
        }
        customer.setCheckedOutAt(LocalDateTime.now());
        return toResponse(customerRepository.save(customer));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse findById(Long id) {
        return toResponse(findCustomerById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse findActiveByTable(String tableNumber) {
        Customer customer = customerRepository
                .findByTableNumberAndCheckedOutAtIsNull(tableNumber)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                    "No active session for table: " + tableNumber));
        return toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void awardLoyaltyPoints(Long customerId, int points) {
        Customer customer = findCustomerById(customerId);
        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + points);
        customerRepository.save(customer);
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
    }

    private CustomerResponse toResponse(Customer c) {
        return CustomerResponse.builder()
                .id(c.getId())
                .tableNumber(c.getTableNumber())
                .name(c.getName())
                .phone(c.getPhone())
                .loyaltyPoints(c.getLoyaltyPoints())
                .preferredLanguage(c.getPreferredLanguage())
                .checkedInAt(c.getCheckedInAt())
                .checkedOutAt(c.getCheckedOutAt())
                .build();
    }
}