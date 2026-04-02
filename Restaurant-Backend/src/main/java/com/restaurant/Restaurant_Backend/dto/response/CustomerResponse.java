package com.restaurant.Restaurant_Backend.dto.response;



import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CustomerResponse {
    private Long id;
    private String tableNumber;
    private String name;
    private String phone;
    private Integer loyaltyPoints;
    private String preferredLanguage;
    private LocalDateTime checkedInAt;
    private LocalDateTime checkedOutAt;
}