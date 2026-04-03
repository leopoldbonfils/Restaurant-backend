package com.restaurant.Restaurant_Backend.dto.request;


import com.restaurant.Restaurant_Backend.model.DietaryTag;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/** POST /api/menu-items and PUT /api/menu-items/{id} */
@Data
public class MenuItemRequest {

    @NotBlank(message = "Item name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private BigDecimal price;

    @NotBlank(message = "Category is required")
    private String category;

    private String imageEmoji;
    private String imageUrl;

    @Min(value = 1, message = "Prep time must be at least 1 minute")
    private Integer prepTimeMinutes = 10;

    private Boolean isSpicy = false;
    private Boolean isAvailable = true;

    private Set<DietaryTag> dietaryTags = new HashSet<>();
    private Set<String> allergens = new HashSet<>();
}