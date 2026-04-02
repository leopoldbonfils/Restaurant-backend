package com.restaurant.Restaurant_Backend.dto.response;



import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class AnalyticsSummaryResponse {
    private long totalOrders;
    private long completedOrders;
    private BigDecimal totalRevenue;
    private BigDecimal averageOrderValue;
    private List<Map<String, Object>> topSellingItems;
    private long uniqueCustomers;
    private long totalLoyaltyPoints;
}