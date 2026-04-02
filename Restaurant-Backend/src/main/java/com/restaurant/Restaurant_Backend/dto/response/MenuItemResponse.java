package com.restaurant.Restaurant_Backend.dto.response;


import com.restaurant.model.DietaryTag;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
public class MenuItemResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private String imageEmoji;
    private String imageUrl;
    private Integer prepTimeMinutes;
    private Boolean isSpicy;
    private Boolean isAvailable;
    private Set<DietaryTag> dietaryTags;
    private Set<String> allergens;
}
