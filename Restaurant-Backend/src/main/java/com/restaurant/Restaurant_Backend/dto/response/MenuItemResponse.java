package com.restaurant.Restaurant_Backend.dto.response;

import com.restaurant.Restaurant_Backend.model.DietaryTag;

import java.math.BigDecimal;
import java.util.Set;

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

    // ── Constructors ──────────────────────────────────────────────────────────

    public MenuItemResponse() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId()                                     { return id; }
    public void setId(Long id)                              { this.id = id; }

    public String getName()                                 { return name; }
    public void   setName(String name)                      { this.name = name; }

    public String getDescription()                          { return description; }
    public void   setDescription(String description)        { this.description = description; }

    public BigDecimal getPrice()                            { return price; }
    public void       setPrice(BigDecimal price)            { this.price = price; }

    public String getCategory()                             { return category; }
    public void   setCategory(String category)              { this.category = category; }

    public String getImageEmoji()                           { return imageEmoji; }
    public void   setImageEmoji(String imageEmoji)          { this.imageEmoji = imageEmoji; }

    public String getImageUrl()                             { return imageUrl; }
    public void   setImageUrl(String imageUrl)              { this.imageUrl = imageUrl; }

    public Integer getPrepTimeMinutes()                     { return prepTimeMinutes; }
    public void    setPrepTimeMinutes(Integer v)            { this.prepTimeMinutes = v; }

    public Boolean getIsSpicy()                             { return isSpicy; }
    public void    setIsSpicy(Boolean isSpicy)              { this.isSpicy = isSpicy; }

    public Boolean getIsAvailable()                         { return isAvailable; }
    public void    setIsAvailable(Boolean isAvailable)      { this.isAvailable = isAvailable; }

    public Set<DietaryTag> getDietaryTags()                 { return dietaryTags; }
    public void setDietaryTags(Set<DietaryTag> dietaryTags) { this.dietaryTags = dietaryTags; }

    public Set<String> getAllergens()                       { return allergens; }
    public void setAllergens(Set<String> allergens)         { this.allergens = allergens; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
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

        public Builder id(Long v)                        { this.id = v; return this; }
        public Builder name(String v)                    { this.name = v; return this; }
        public Builder description(String v)             { this.description = v; return this; }
        public Builder price(BigDecimal v)               { this.price = v; return this; }
        public Builder category(String v)                { this.category = v; return this; }
        public Builder imageEmoji(String v)              { this.imageEmoji = v; return this; }
        public Builder imageUrl(String v)                { this.imageUrl = v; return this; }
        public Builder prepTimeMinutes(Integer v)        { this.prepTimeMinutes = v; return this; }
        public Builder isSpicy(Boolean v)                { this.isSpicy = v; return this; }
        public Builder isAvailable(Boolean v)            { this.isAvailable = v; return this; }
        public Builder dietaryTags(Set<DietaryTag> v)   { this.dietaryTags = v; return this; }
        public Builder allergens(Set<String> v)          { this.allergens = v; return this; }

        public MenuItemResponse build() {
            MenuItemResponse r = new MenuItemResponse();
            r.id = this.id;
            r.name = this.name;
            r.description = this.description;
            r.price = this.price;
            r.category = this.category;
            r.imageEmoji = this.imageEmoji;
            r.imageUrl = this.imageUrl;
            r.prepTimeMinutes = this.prepTimeMinutes;
            r.isSpicy = this.isSpicy;
            r.isAvailable = this.isAvailable;
            r.dietaryTags = this.dietaryTags;
            r.allergens = this.allergens;
            return r;
        }
    }
}