package com.restaurant.Restaurant_Backend.config;



import com.restaurant.model.DietaryTag;
import com.restaurant.model.MenuItem;
import com.restaurant.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Seeds the database with Rwandan restaurant menu items on the first run.
 * Skipped if menu_items table already has records.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final MenuItemRepository menuItemRepository;

    @Bean
    public CommandLineRunner seedMenu() {
        return args -> {
            if (menuItemRepository.count() > 0) {
                log.info("Menu already seeded – skipping.");
                return;
            }
            log.info("Seeding initial menu data...");

            menuItemRepository.saveAll(List.of(

                // ── Main Course ───────────────────────────────────────────
                MenuItem.builder()
                        .name("Brochette").category("Main Course").imageEmoji("🍢")
                        .description("Grilled meat skewers, perfectly seasoned")
                        .price(new BigDecimal("3000")).prepTimeMinutes(15)
                        .isSpicy(false).isAvailable(true)
                        .dietaryTags(Set.of(DietaryTag.HALAL))
                        .allergens(Set.of("Meat")).build(),

                MenuItem.builder()
                        .name("Ugali").category("Main Course").imageEmoji("🍚")
                        .description("Traditional corn meal, a Rwandan staple")
                        .price(new BigDecimal("1500")).prepTimeMinutes(10)
                        .isSpicy(false).isAvailable(true)
                        .dietaryTags(Set.of(DietaryTag.VEGAN, DietaryTag.GLUTEN_FREE))
                        .allergens(Set.of()).build(),

                MenuItem.builder()
                        .name("Isombe").category("Main Course").imageEmoji("🥬")
                        .description("Cassava leaves stew with palm oil")
                        .price(new BigDecimal("2500")).prepTimeMinutes(20)
                        .isSpicy(true).isAvailable(true)
                        .dietaryTags(Set.of(DietaryTag.VEGAN))
                        .allergens(Set.of()).build(),

                MenuItem.builder()
                        .name("Ibihaza").category("Main Course").imageEmoji("🎃")
                        .description("Pumpkin cooked with beans – hearty & filling")
                        .price(new BigDecimal("2000")).prepTimeMinutes(18)
                        .isSpicy(false).isAvailable(true)
                        .dietaryTags(Set.of(DietaryTag.VEGAN, DietaryTag.GLUTEN_FREE))
                        .allergens(Set.of()).build(),

                // ── Appetizers ────────────────────────────────────────────
                MenuItem.builder()
                        .name("Sambaza").category("Appetizer").imageEmoji("🐟")
                        .description("Crispy fried small lake fish")
                        .price(new BigDecimal("2000")).prepTimeMinutes(12)
                        .isSpicy(false).isAvailable(true)
                        .dietaryTags(Set.of(DietaryTag.HALAL))
                        .allergens(Set.of("Fish")).build(),

                MenuItem.builder()
                        .name("Akabanga Wings").category("Appetizer").imageEmoji("🍗")
                        .description("Chicken wings tossed in Akabanga chili oil")
                        .price(new BigDecimal("2500")).prepTimeMinutes(15)
                        .isSpicy(true).isAvailable(true)
                        .dietaryTags(Set.of(DietaryTag.HALAL))
                        .allergens(Set.of("Poultry")).build(),

                // ── Sides ─────────────────────────────────────────────────
                MenuItem.builder()
                        .name("Chips").category("Side").imageEmoji("🍟")
                        .description("Crispy golden French fries")
                        .price(new BigDecimal("2000")).prepTimeMinutes(8)
                        .isSpicy(false).isAvailable(true)
                        .dietaryTags(Set.of(DietaryTag.VEGAN))
                        .allergens(Set.of()).build(),

                MenuItem.builder()
                        .name("Mizuzu").category("Side").imageEmoji("🍌")
                        .description("Deep-fried plantain slices")
                        .price(new BigDecimal("1200")).prepTimeMinutes(7)
                        .isSpicy(false).isAvailable(true)
                        .dietaryTags(Set.of(DietaryTag.VEGAN, DietaryTag.GLUTEN_FREE))
                        .allergens(Set.of()).build(),

                // ── Drinks ────────────────────────────────────────────────
                MenuItem.builder()
                        .name("Primus Beer").category("Drinks").imageEmoji("🍺")
                        .description("Rwanda's iconic local lager")
                        .price(new BigDecimal("1500")).prepTimeMinutes(2)
                        .isSpicy(false).isAvailable(true)
                        .dietaryTags(Set.of()).allergens(Set.of("Gluten")).build(),

                MenuItem.builder()
                        .name("Fanta").category("Drinks").imageEmoji("🥤")
                        .description("Chilled soft drink – Orange or Citron")
                        .price(new BigDecimal("800")).prepTimeMinutes(1)
                        .isSpicy(false).isAvailable(true)
                        .dietaryTags(Set.of(DietaryTag.VEGAN)).allergens(Set.of()).build(),

                MenuItem.builder()
                        .name("Ikawa (Coffee)").category("Drinks").imageEmoji("☕")
                        .description("Premium single-origin Rwandan coffee")
                        .price(new BigDecimal("1000")).prepTimeMinutes(3)
                        .isSpicy(false).isAvailable(true)
                        .dietaryTags(Set.of(DietaryTag.VEGAN)).allergens(Set.of()).build(),

                MenuItem.builder()
                        .name("Passion Fruit Juice").category("Drinks").imageEmoji("🍹")
                        .description("Freshly squeezed passion fruit, lightly sweetened")
                        .price(new BigDecimal("1200")).prepTimeMinutes(3)
                        .isSpicy(false).isAvailable(true)
                        .dietaryTags(Set.of(DietaryTag.VEGAN, DietaryTag.GLUTEN_FREE))
                        .allergens(Set.of()).build()
            ));

            log.info("✅ Menu seeded with {} items.", menuItemRepository.count());
        };
    }
}