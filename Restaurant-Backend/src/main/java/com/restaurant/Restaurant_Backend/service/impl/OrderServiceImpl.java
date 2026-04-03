package com.restaurant.Restaurant_Backend.service.impl;

import com.restaurant.Restaurant_Backend.dto.request.PlaceOrderRequest;
import com.restaurant.Restaurant_Backend.dto.request.UpdateOrderStatusRequest;
import com.restaurant.Restaurant_Backend.dto.response.AnalyticsSummaryResponse;
import com.restaurant.Restaurant_Backend.dto.response.OrderItemResponse;
import com.restaurant.Restaurant_Backend.dto.response.OrderResponse;
import com.restaurant.Restaurant_Backend.exception.BadRequestException;
import com.restaurant.Restaurant_Backend.exception.ResourceNotFoundException;
import com.restaurant.Restaurant_Backend.model.*;
import com.restaurant.Restaurant_Backend.repository.CustomerRepository;
import com.restaurant.Restaurant_Backend.repository.MenuItemRepository;
import com.restaurant.Restaurant_Backend.repository.OrderRepository;
import com.restaurant.Restaurant_Backend.service.CustomerService;
import com.restaurant.Restaurant_Backend.service.OrderService;
import com.restaurant.Restaurant_Backend.websocket.OrderNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final MenuItemRepository menuItemRepository;
    private final CustomerService customerService;
    private final OrderNotificationService notificationService;

    @Override
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Customer", request.getCustomerId()));

        Order order = Order.builder()
                .customer(customer)
                .tableNumber(request.getTableNumber())
                .specialRequests(request.getSpecialRequests())
                .status(OrderStatus.PENDING)
                .build();

        int maxPrepTime = 0;
        for (PlaceOrderRequest.OrderItemRequest itemReq : request.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemReq.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                        "MenuItem", itemReq.getMenuItemId()));

            if (!menuItem.getIsAvailable()) {
                throw new BadRequestException(
                    menuItem.getName() + " is currently unavailable.");
            }

            OrderItem orderItem = OrderItem.builder()
                    .menuItem(menuItem)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(menuItem.getPrice())
                    .itemNotes(itemReq.getItemNotes())
                    .build();

            order.addItem(orderItem);

            if (menuItem.getPrepTimeMinutes() > maxPrepTime) {
                maxPrepTime = menuItem.getPrepTimeMinutes();
            }
        }

        order.setEstimatedPrepMinutes(maxPrepTime + 5);
        Order saved = orderRepository.save(order);

        int pointsEarned = saved.getTotalAmount()
                .divide(BigDecimal.valueOf(100), RoundingMode.DOWN)
                .intValue();
        customerService.awardLoyaltyPoints(customer.getId(), pointsEarned);

        notificationService.notifyNewOrder(saved);
        return toResponse(saved);
    }

    @Override
    public OrderResponse updateStatus(Long orderId, UpdateOrderStatusRequest request) {
        Order order = findOrderById(orderId);
        OrderStatus previous = order.getStatus();
        OrderStatus next = request.getStatus();

        validateTransition(previous, next);

        order.setStatus(next);
        Order saved = orderRepository.save(order);
        notificationService.notifyOrderStatusChanged(saved, previous);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        return toResponse(findOrderById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findActive() {
        return orderRepository.findByStatusInOrderByCreatedAtAsc(
                List.of(OrderStatus.PENDING, OrderStatus.PREPARING,
                        OrderStatus.READY, OrderStatus.DELIVERED))
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findByStatus(OrderStatus status) {
        return orderRepository.findByStatusOrderByCreatedAtAsc(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findByTable(String tableNumber) {
        return orderRepository.findByTableNumberOrderByCreatedAtDesc(tableNumber).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findByCustomer(Long customerId) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse cancelOrder(Long orderId) {
        Order order = findOrderById(orderId);
        if (order.getStatus() == OrderStatus.READY
                || order.getStatus() == OrderStatus.DELIVERED
                || order.getStatus() == OrderStatus.PAID) {
            throw new BadRequestException(
                "Cannot cancel an order that is " + order.getStatus());
        }
        OrderStatus previous = order.getStatus();
        order.setStatus(OrderStatus.CANCELLED);
        Order saved = orderRepository.save(order);
        notificationService.notifyOrderStatusChanged(saved, previous);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AnalyticsSummaryResponse getAnalyticsSummary() {
        LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = LocalDateTime.now();

        long total = orderRepository.count();
        long completed = orderRepository.countCompletedBetween(start, end);
        BigDecimal revenue = orderRepository.sumRevenueBetween(start, end);
        BigDecimal avg = completed > 0
                ? revenue.divide(BigDecimal.valueOf(completed), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        List<Object[]> rawTop = orderRepository.findTopSellingItems();
        List<Map<String, Object>> topItems = new ArrayList<>();
        for (Object[] row : rawTop) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("name", row[0]);
            entry.put("quantitySold", row[1]);
            topItems.add(entry);
        }

        long uniqueCustomers = customerRepository.count();
        long totalPoints = customerRepository.findAll().stream()
                .mapToLong(c -> c.getLoyaltyPoints())
                .sum();

        return AnalyticsSummaryResponse.builder()
                .totalOrders(total)
                .completedOrders(completed)
                .totalRevenue(revenue)
                .averageOrderValue(avg)
                .topSellingItems(topItems)
                .uniqueCustomers(uniqueCustomers)
                .totalLoyaltyPoints(totalPoints)
                .build();
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
    }

    private void validateTransition(OrderStatus from, OrderStatus to) {
        boolean valid = switch (from) {
            case PENDING   -> to == OrderStatus.PREPARING || to == OrderStatus.CANCELLED;
            case PREPARING -> to == OrderStatus.READY    || to == OrderStatus.CANCELLED;
            case READY     -> to == OrderStatus.DELIVERED;
            case DELIVERED -> to == OrderStatus.PAID;
            case PAID, CANCELLED -> false;
        };
        if (!valid) {
            throw new BadRequestException(
                "Cannot transition from " + from + " to " + to);
        }
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(i -> OrderItemResponse.builder()
                        .id(i.getId())
                        .menuItemId(i.getMenuItem().getId())
                        .menuItemName(i.getMenuItem().getName())
                        .menuItemEmoji(i.getMenuItem().getImageEmoji())
                        .quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice())
                        .lineTotal(i.getLineTotal())
                        .itemNotes(i.getItemNotes())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomer().getId())
                .tableNumber(order.getTableNumber())
                .status(order.getStatus())
                .items(itemResponses)
                .totalAmount(order.getTotalAmount())
                .specialRequests(order.getSpecialRequests())
                .estimatedPrepMinutes(order.getEstimatedPrepMinutes())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .completedAt(order.getCompletedAt())
                .build();
    }
}