package com.restaurant.Restaurant_Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A single dish or drink available on the restaurant menu.
 *
 * Tables created automatically by Hibernate:
 *   menu_items            — main entity table
 *   menu_item_dietary_tags — join table for DietaryTag enum values
 *   menu_item_allergens   — join table for free-text allergen strings
 */
@Entity
@Table(name = "menu_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    /**
     * Menu section: "Main Course", "Appetizer", "Side", "Drinks".
     * Drives category grouping on the customer menu screen.
     */
    @NotBlank
    @Column(nullable = false, length = 50)
    private String category;

    /** Emoji shorthand displayed on the frontend. e.g. "🍢" */
    @Column(name = "image_emoji", length = 10)
    private String imageEmoji;

    /** Full image URL — used if imageEmoji is not set. */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * How long the item takes to prepare in minutes.
     * Used to calculate the estimated wait time shown to customers.
     */
    @Column(name = "prep_time_minutes")
    @Builder.Default
    private Integer prepTimeMinutes = 10;

    @Column(name = "is_spicy")
    @Builder.Default
    private Boolean isSpicy = false;

    /**
     * Admin can toggle this off when an item is out of stock.
     * Unavailable items are hidden from the customer menu.
     */
    @Column(name = "is_available")
    @Builder.Default
    private Boolean isAvailable = true;

    /**
     * Dietary classification tags.
     *
     * Stored in a separate join table "menu_item_dietary_tags":
     *   menu_item_id | tag
     *   -------------|----------
     *   1            | VEGAN
     *   1            | GLUTEN_FREE
     *
     * FetchType.EAGER — always loaded with the menu item
     * (small set, needed on every menu request).
     */
    @ElementCollection(targetClass = DietaryTag.class, fetch = FetchType.EAGER)
    @CollectionTable(
        name = "menu_item_dietary_tags",
        joinColumns = @JoinColumn(name = "menu_item_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "tag", length = 20)
    @Builder.Default
    private Set<DietaryTag> dietaryTags = new HashSet<>();

    /**
     * Free-text allergen labels, e.g. "Fish", "Gluten", "Nuts".
     *
     * Stored in join table "menu_item_allergens":
     *   menu_item_id | allergen
     *   -------------|----------
     *   4            | Fish
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "menu_item_allergens",
        joinColumns = @JoinColumn(name = "menu_item_id")
    )
    @Column(name = "allergen", length = 50)
    @Builder.Default
    private Set<String> allergens = new HashSet<>();
}