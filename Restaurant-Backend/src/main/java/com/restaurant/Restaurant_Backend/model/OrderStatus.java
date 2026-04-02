package com.restaurant.Restaurant_Backend.model;

public enum OrderStatus {
    PENDING,    // Received by kitchen, not yet started
    PREPARING,  // Kitchen is actively cooking
    READY,      // Food ready, waiter to deliver
    DELIVERED,  // Delivered to table, awaiting payment
    PAID,       // Payment confirmed — order complete
    CANCELLED   // Cancelled before completion
}