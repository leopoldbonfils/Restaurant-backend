package com.restaurant.Restaurant_Backend.config;

import com.restaurant.Restaurant_Backend.model.DietaryTag;
import com.restaurant.Restaurant_Backend.model.MenuItem;
import com.restaurant.Restaurant_Backend.model.Role;
import com.restaurant.Restaurant_Backend.model.User;
import com.restaurant.Restaurant_Backend.repository.MenuItemRepository;
import com.restaurant.Restaurant_Backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Seeds the database on first startup:
 *   1. Demo users  (admin, kitchen)  — skipped if already present
 *   2. Rwandan menu items            — skipped if already present
 *
 * NOTE: Lombok is NOT used here — NetBeans compatibility issue.
 *       All builder calls use the manual builder in User.java.
 */
@Configuration
public class DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final MenuItemRepository menuItemRepository;
    private final UserRepository     userRepository;
    private final PasswordEncoder    passwordEncoder;

    public DataSeeder(MenuItemRepository menuItemRepository,
                      UserRepository userRepository,
                      PasswordEncoder passwordEncoder) {
        this.menuItemRepository = menuItemRepository;
        this.userRepository     = userRepository;
        this.passwordEncoder    = passwordEncoder;
    }

    // ── 1. Seed staff accounts ────────────────────────────────────────────

    @Bean
    public CommandLineRunner seedUsers() {
        return args -> {
            if (userRepository.existsByEmail("admin@demo.rw")) {
                log.info("Staff accounts already seeded – skipping.");
                return;
            }

            userRepository.saveAll(List.of(
                User.builder()
                    .email("admin@demo.rw")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Admin User")
                    .role(Role.ADMIN)
                    .isEnabled(true)
                    .build(),

                User.builder()
                    .email("kitchen@demo.rw")
                    .password(passwordEncoder.encode("kitchen123"))
                    .fullName("Kitchen Staff")
                    .role(Role.KITCHEN)
                    .isEnabled(true)
                    .build()
            ));

            log.info("✅ Demo staff accounts seeded (admin + kitchen).");
        };
    }

    // ── 2. Seed menu items ─────────────────────────────────────────────────

    @Bean
    public CommandLineRunner seedMenu() {
        return args -> {
            if (menuItemRepository.count() > 0) {
                log.info("Menu already seeded – skipping.");
                return;
            }
            log.info("Seeding initial menu data…");

            menuItemRepository.saveAll(List.of(

                // ── Main Course ───────────────────────────────────────────
                buildMenuItem("Brochette",  "Main Course", "🍢",
                    "Grilled meat skewers, perfectly seasoned",
                    "3000", 15, false, Set.of(DietaryTag.HALAL), Set.of("Meat")),

                buildMenuItem("Ugali", "Main Course", "🍚",
                    "Traditional corn meal, a Rwandan staple",
                    "1500", 10, false, Set.of(DietaryTag.VEGAN, DietaryTag.GLUTEN_FREE), Set.of()),

                buildMenuItem("Isombe", "Main Course", "🥬",
                    "Cassava leaves stew with palm oil",
                    "2500", 20, true, Set.of(DietaryTag.VEGAN), Set.of()),

                buildMenuItem("Ibihaza", "Main Course", "🎃",
                    "Pumpkin cooked with beans – hearty & filling",
                    "2000", 18, false, Set.of(DietaryTag.VEGAN, DietaryTag.GLUTEN_FREE), Set.of()),

                // ── Appetizers ────────────────────────────────────────────
                buildMenuItem("Sambaza", "Appetizer", "🐟",
                    "Crispy fried small lake fish",
                    "2000", 12, false, Set.of(DietaryTag.HALAL), Set.of("Fish")),

                buildMenuItem("Akabanga Wings", "Appetizer", "🍗",
                    "Chicken wings tossed in Akabanga chili oil",
                    "2500", 15, true, Set.of(DietaryTag.HALAL), Set.of("Poultry")),

                // ── Sides ─────────────────────────────────────────────────
                buildMenuItem("Chips", "Side", "🍟",
                    "Crispy golden French fries",
                    "2000", 8, false, Set.of(DietaryTag.VEGAN), Set.of()),

                buildMenuItem("Mizuzu", "Side", "🍌",
                    "Deep-fried plantain slices",
                    "1200", 7, false, Set.of(DietaryTag.VEGAN, DietaryTag.GLUTEN_FREE), Set.of()),

                // ── Drinks ────────────────────────────────────────────────
                buildMenuItem("Primus Beer", "Drinks", "🍺",
                    "Rwanda's iconic local lager",
                    "1500", 2, false, Set.of(), Set.of("Gluten")),

                buildMenuItem("Fanta", "Drinks", "🥤",
                    "Chilled soft drink – Orange or Citron",
                    "800", 1, false, Set.of(DietaryTag.VEGAN), Set.of()),

                buildMenuItem("Ikawa (Coffee)", "Drinks", "☕",
                    "Premium single-origin Rwandan coffee",
                    "1000", 3, false, Set.of(DietaryTag.VEGAN), Set.of()),

                buildMenuItem("Passion Fruit Juice", "Drinks", "🍹",
                    "Freshly squeezed passion fruit, lightly sweetened",
                    "1200", 3, false, Set.of(DietaryTag.VEGAN, DietaryTag.GLUTEN_FREE), Set.of())
            ));

            log.info("✅ Menu seeded with {} items.", menuItemRepository.count());
        };
    }

    // ── Private builder helper ─────────────────────────────────────────────

    private MenuItem buildMenuItem(String name, String category, String emoji,
                                   String description, String price,
                                   int prepTime, boolean isSpicy,
                                   Set<DietaryTag> dietaryTags, Set<String> allergens) {
        MenuItem item = new MenuItem();
        item.setName(name);
        item.setCategory(category);
        item.setImageEmoji(emoji);
        item.setDescription(description);
        item.setPrice(new BigDecimal(price));
        item.setPrepTimeMinutes(prepTime);
        item.setIsSpicy(isSpicy);
        item.setIsAvailable(true);
        item.setDietaryTags(dietaryTags);
        item.setAllergens(allergens);
        return item;
    }
}