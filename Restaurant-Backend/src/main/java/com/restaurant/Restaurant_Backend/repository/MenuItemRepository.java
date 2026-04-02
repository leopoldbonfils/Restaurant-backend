package com.restaurant.Restaurant_Backend.repository;



import com.restaurant.model.DietaryTag;
import com.restaurant.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByIsAvailableTrue();

    List<MenuItem> findByCategoryIgnoreCase(String category);

    List<MenuItem> findByCategoryIgnoreCaseAndIsAvailableTrue(String category);

    /** Items that include ALL the requested dietary tags. */
    @Query("SELECT m FROM MenuItem m WHERE :tag MEMBER OF m.dietaryTags AND m.isAvailable = true")
    List<MenuItem> findAvailableByDietaryTag(@Param("tag") DietaryTag tag);

    List<MenuItem> findByIsSpicyTrue();

    @Query("SELECT DISTINCT m.category FROM MenuItem m ORDER BY m.category")
    List<String> findAllCategories();
}