package com.restaurant.Restaurant_Backend.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class AnalyticsSummaryResponse {

    private long totalOrders;
    private long completedOrders;
    private BigDecimal totalRevenue;
    private BigDecimal averageOrderValue;
    private List<Map<String, Object>> topSellingItems;
    private long uniqueCustomers;
    private long totalLoyaltyPoints;

    // ── Constructors ──────────────────────────────────────────────────────────

    public AnalyticsSummaryResponse() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public long getTotalOrders()                                    { return totalOrders; }
    public void setTotalOrders(long totalOrders)                    { this.totalOrders = totalOrders; }

    public long getCompletedOrders()                                { return completedOrders; }
    public void setCompletedOrders(long completedOrders)            { this.completedOrders = completedOrders; }

    public BigDecimal getTotalRevenue()                             { return totalRevenue; }
    public void       setTotalRevenue(BigDecimal totalRevenue)      { this.totalRevenue = totalRevenue; }

    public BigDecimal getAverageOrderValue()                        { return averageOrderValue; }
    public void       setAverageOrderValue(BigDecimal v)            { this.averageOrderValue = v; }

    public List<Map<String, Object>> getTopSellingItems()           { return topSellingItems; }
    public void setTopSellingItems(List<Map<String, Object>> items) { this.topSellingItems = items; }

    public long getUniqueCustomers()                                { return uniqueCustomers; }
    public void setUniqueCustomers(long uniqueCustomers)            { this.uniqueCustomers = uniqueCustomers; }

    public long getTotalLoyaltyPoints()                             { return totalLoyaltyPoints; }
    public void setTotalLoyaltyPoints(long totalLoyaltyPoints)      { this.totalLoyaltyPoints = totalLoyaltyPoints; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private long totalOrders;
        private long completedOrders;
        private BigDecimal totalRevenue;
        private BigDecimal averageOrderValue;
        private List<Map<String, Object>> topSellingItems;
        private long uniqueCustomers;
        private long totalLoyaltyPoints;

        public Builder totalOrders(long v)                          { this.totalOrders = v; return this; }
        public Builder completedOrders(long v)                      { this.completedOrders = v; return this; }
        public Builder totalRevenue(BigDecimal v)                   { this.totalRevenue = v; return this; }
        public Builder averageOrderValue(BigDecimal v)              { this.averageOrderValue = v; return this; }
        public Builder topSellingItems(List<Map<String, Object>> v) { this.topSellingItems = v; return this; }
        public Builder uniqueCustomers(long v)                      { this.uniqueCustomers = v; return this; }
        public Builder totalLoyaltyPoints(long v)                   { this.totalLoyaltyPoints = v; return this; }

        public AnalyticsSummaryResponse build() {
            AnalyticsSummaryResponse r = new AnalyticsSummaryResponse();
            r.totalOrders       = this.totalOrders;
            r.completedOrders   = this.completedOrders;
            r.totalRevenue      = this.totalRevenue;
            r.averageOrderValue = this.averageOrderValue;
            r.topSellingItems   = this.topSellingItems;
            r.uniqueCustomers   = this.uniqueCustomers;
            r.totalLoyaltyPoints = this.totalLoyaltyPoints;
            return r;
        }
    }
}