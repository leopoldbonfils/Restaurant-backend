package com.restaurant.Restaurant_Backend.controller;

import com.restaurant.Restaurant_Backend.dto.request.PlaceOrderRequest;
import com.restaurant.Restaurant_Backend.dto.request.UpdateOrderStatusRequest;
import com.restaurant.Restaurant_Backend.dto.response.AnalyticsSummaryResponse;
import com.restaurant.Restaurant_Backend.dto.response.ApiResponse;
import com.restaurant.Restaurant_Backend.dto.response.OrderResponse;
import com.restaurant.Restaurant_Backend.model.OrderStatus;
import com.restaurant.Restaurant_Backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(
            @Valid @RequestBody PlaceOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Order placed successfully.",
                        orderService.placeOrder(request)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Order status updated.",
                orderService.updateStatus(id, request)));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Order cancelled.",
                orderService.cancelOrder(id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(orderService.findAll()));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getActive() {
        return ResponseEntity.ok(ApiResponse.ok(orderService.findActive()));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getByStatus(
            @PathVariable OrderStatus status) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.findByStatus(status)));
    }

    @GetMapping("/table/{tableNumber}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getByTable(
            @PathVariable String tableNumber) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.findByTable(tableNumber)));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getByCustomer(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.findByCustomer(customerId)));
    }

    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse<AnalyticsSummaryResponse>> getAnalytics() {
        return ResponseEntity.ok(ApiResponse.ok(orderService.getAnalyticsSummary()));
    }
}