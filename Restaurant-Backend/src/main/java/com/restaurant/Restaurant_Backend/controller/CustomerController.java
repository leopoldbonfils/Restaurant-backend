package com.restaurant.Restaurant_Backend.controller;

import com.restaurant.Restaurant_Backend.dto.request.CustomerCheckInRequest;
import com.restaurant.Restaurant_Backend.dto.response.ApiResponse;
import com.restaurant.Restaurant_Backend.dto.response.CustomerResponse;
import com.restaurant.Restaurant_Backend.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/check-in")
    public ResponseEntity<ApiResponse<CustomerResponse>> checkIn(
            @Valid @RequestBody CustomerCheckInRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Customer checked in successfully.",
                        customerService.checkIn(request)));
    }

    @PatchMapping("/{id}/check-out")
    public ResponseEntity<ApiResponse<CustomerResponse>> checkOut(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Customer checked out.",
                customerService.checkOut(id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(customerService.findById(id)));
    }

    @GetMapping("/table/{tableNumber}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getActiveByTable(
            @PathVariable String tableNumber) {
        return ResponseEntity.ok(ApiResponse.ok(
                customerService.findActiveByTable(tableNumber)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(customerService.findAll()));
    }
}