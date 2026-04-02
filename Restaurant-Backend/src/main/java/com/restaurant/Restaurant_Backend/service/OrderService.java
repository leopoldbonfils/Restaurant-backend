package com.restaurant.Restaurant_Backend.service;


import com.restaurant.dto.request.PlaceOrderRequest;
import com.restaurant.dto.request.UpdateOrderStatusRequest;
import com.restaurant.dto.response.AnalyticsSummaryResponse;
import com.restaurant.dto.response.OrderResponse;
import com.restaurant.model.OrderStatus;

import java.util.List;

public interface OrderService {

    /** Customer places a new order. */
    OrderResponse placeOrder(PlaceOrderRequest request);

    /** Kitchen or waiter advances the order status. */
    OrderResponse updateStatus(Long orderId, UpdateOrderStatusRequest request);

    OrderResponse findById(Long id);

    /** All orders – admin / kitchen overview. */
    List<OrderResponse> findAll();

    /** Active orders only (PENDING, PREPARING, READY, DELIVERED). */
    List<OrderResponse> findActive();

    /** Filter by a specific status. */
    List<OrderResponse> findByStatus(OrderStatus status);

    /** All orders for a given table number. */
    List<OrderResponse> findByTable(String tableNumber);

    /** Order history for a specific customer. */
    List<OrderResponse> findByCustomer(Long customerId);

    /** Cancel an order (only allowed while PENDING or PREPARING). */
    OrderResponse cancelOrder(Long orderId);

    /** Analytics summary for the admin dashboard. */
    AnalyticsSummaryResponse getAnalyticsSummary();
}
