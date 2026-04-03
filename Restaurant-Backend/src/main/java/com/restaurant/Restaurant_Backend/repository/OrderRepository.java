package com.restaurant.Restaurant_Backend.repository;

import com.restaurant.Restaurant_Backend.model.Order;
import com.restaurant.Restaurant_Backend.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // ── Kitchen & waiter queries ───────────────────────────────────────────
    List<Order> findByStatusOrderByCreatedAtAsc(OrderStatus status);

    List<Order> findByStatusInOrderByCreatedAtAsc(List<OrderStatus> statuses);

    List<Order> findByTableNumberOrderByCreatedAtDesc(String tableNumber);

    List<Order> findByTableNumberAndStatusIn(String tableNumber, List<OrderStatus> statuses);

    // ── Customer history ───────────────────────────────────────────────────
    List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    // ── Analytics ─────────────────────────────────────────────────────────
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'PAID' " +
           "AND o.createdAt BETWEEN :start AND :end")
    long countCompletedBetween(@Param("start") LocalDateTime start,
                               @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o " +
           "WHERE o.status = 'PAID' AND o.createdAt BETWEEN :start AND :end")
    BigDecimal sumRevenueBetween(@Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);

    /** Popular items: returns Object[]{itemName, totalQtySold} sorted desc. */
    @Query("SELECT oi.menuItem.name, SUM(oi.quantity) AS qty " +
           "FROM OrderItem oi WHERE oi.order.status = 'PAID' " +
           "GROUP BY oi.menuItem.name ORDER BY qty DESC")
    List<Object[]> findTopSellingItems();
}