package com.restaurant.Restaurant_Backend.dto.request;

import com.restaurant.Restaurant_Backend.model.DietaryTag;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/** POST /api/menu-items and PUT /api/menu-items/{id} */
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
    private Set<String>     allergens   = new HashSet<>();

    // ── Constructors ──────────────────────────────────────────────────────────

    public MenuItemRequest() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getName()                             { return name; }
    public void   setName(String name)                  { this.name = name; }

    public String getDescription()                      { return description; }
    public void   setDescription(String description)    { this.description = description; }

    public BigDecimal getPrice()                        { return price; }
    public void       setPrice(BigDecimal price)        { this.price = price; }

    public String getCategory()                         { return category; }
    public void   setCategory(String category)          { this.category = category; }

    public String getImageEmoji()                       { return imageEmoji; }
    public void   setImageEmoji(String imageEmoji)      { this.imageEmoji = imageEmoji; }

    public String getImageUrl()                         { return imageUrl; }
    public void   setImageUrl(String imageUrl)          { this.imageUrl = imageUrl; }

    public Integer getPrepTimeMinutes()                 { return prepTimeMinutes; }
    public void    setPrepTimeMinutes(Integer v)        { this.prepTimeMinutes = v; }

    public Boolean getIsSpicy()                         { return isSpicy; }
    public void    setIsSpicy(Boolean isSpicy)          { this.isSpicy = isSpicy; }

    public Boolean getIsAvailable()                     { return isAvailable; }
    public void    setIsAvailable(Boolean isAvailable)  { this.isAvailable = isAvailable; }

    public Set<DietaryTag> getDietaryTags()             { return dietaryTags; }
    public void setDietaryTags(Set<DietaryTag> tags)    { this.dietaryTags = tags; }

    public Set<String> getAllergens()                   { return allergens; }
    public void setAllergens(Set<String> allergens)     { this.allergens = allergens; }
}