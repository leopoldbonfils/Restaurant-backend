package com.restaurant.Restaurant_Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "menu_items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @NotNull
    @DecimalMin("0.0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String category;

    @Column(name = "image_emoji", length = 10)
    private String imageEmoji;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "prep_time_minutes")
    private Integer prepTimeMinutes = 10;

    @Column(name = "is_spicy")
    private Boolean isSpicy = false;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @ElementCollection(targetClass = DietaryTag.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "menu_item_dietary_tags",
                     joinColumns = @JoinColumn(name = "menu_item_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "tag", length = 20)
    private Set<DietaryTag> dietaryTags = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "menu_item_allergens",
                     joinColumns = @JoinColumn(name = "menu_item_id"))
    @Column(name = "allergen", length = 50)
    private Set<String> allergens = new HashSet<>();

    // ── Constructors ─────────────────────────────────────────────────────────
    public MenuItem() {}

    // ── Getters ──────────────────────────────────────────────────────────────
    public Long    getId()              { return id; }
    public String  getName()            { return name; }
    public String  getDescription()     { return description; }
    public BigDecimal getPrice()        { return price; }
    public String  getCategory()        { return category; }
    public String  getImageEmoji()      { return imageEmoji; }
    public String  getImageUrl()        { return imageUrl; }
    public Integer getPrepTimeMinutes() { return prepTimeMinutes; }
    public Boolean getIsSpicy()         { return isSpicy; }
    public Boolean getIsAvailable()     { return isAvailable; }
    public Set<DietaryTag> getDietaryTags() { return dietaryTags; }
    public Set<String>     getAllergens()    { return allergens; }

    // ── Setters ──────────────────────────────────────────────────────────────
    public void setId(Long id)                         { this.id = id; }
    public void setName(String name)                   { this.name = name; }
    public void setDescription(String description)     { this.description = description; }
    public void setPrice(BigDecimal price)             { this.price = price; }
    public void setCategory(String category)           { this.category = category; }
    public void setImageEmoji(String imageEmoji)       { this.imageEmoji = imageEmoji; }
    public void setImageUrl(String imageUrl)           { this.imageUrl = imageUrl; }
    public void setPrepTimeMinutes(Integer v)          { this.prepTimeMinutes = v; }
    public void setIsSpicy(Boolean isSpicy)            { this.isSpicy = isSpicy; }
    public void setIsAvailable(Boolean isAvailable)    { this.isAvailable = isAvailable; }
    public void setDietaryTags(Set<DietaryTag> tags)   { this.dietaryTags = tags; }
    public void setAllergens(Set<String> allergens)    { this.allergens = allergens; }

    // ── Builder ───────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final MenuItem item = new MenuItem();

        public Builder name(String v)            { item.name = v;            return this; }
        public Builder description(String v)     { item.description = v;     return this; }
        public Builder price(BigDecimal v)       { item.price = v;           return this; }
        public Builder category(String v)        { item.category = v;        return this; }
        public Builder imageEmoji(String v)      { item.imageEmoji = v;      return this; }
        public Builder imageUrl(String v)        { item.imageUrl = v;        return this; }
        public Builder prepTimeMinutes(Integer v){ item.prepTimeMinutes = v; return this; }
        public Builder isSpicy(Boolean v)        { item.isSpicy = v;         return this; }
        public Builder isAvailable(Boolean v)    { item.isAvailable = v;     return this; }
        public Builder dietaryTags(Set<DietaryTag> v) { item.dietaryTags = v; return this; }
        public Builder allergens(Set<String> v)  { item.allergens = v;       return this; }

        public MenuItem build() { return item; }
    }
}