package com.restaurant.Restaurant_Backend.service;


import com.restaurant.Restaurant_Backend.dto.request.MenuItemRequest;
import com.restaurant.Restaurant_Backend.dto.response.MenuItemResponse;
import com.restaurant.Restaurant_Backend.model.DietaryTag;

import java.util.List;

public interface MenuItemService {

    MenuItemResponse create(MenuItemRequest request);

    MenuItemResponse update(Long id, MenuItemRequest request);

    void delete(Long id);

    MenuItemResponse findById(Long id);

    /** Full menu (admin view). */
    List<MenuItemResponse> findAll();

    /** Only available items (customer-facing menu). */
    List<MenuItemResponse> findAllAvailable();

    List<MenuItemResponse> findByCategory(String category);

    List<MenuItemResponse> findByDietaryTag(DietaryTag tag);

    List<String> findAllCategories();

    /** Toggle isAvailable flag. */
    MenuItemResponse toggleAvailability(Long id);
}
