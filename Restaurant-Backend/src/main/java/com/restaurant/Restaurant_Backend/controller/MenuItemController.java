package com.restaurant.Restaurant_Backend.controller;


import com.restaurant.Restaurant_Backend.dto.request.MenuItemRequest;
import com.restaurant.Restaurant_Backend.dto.response.ApiResponse;
import com.restaurant.Restaurant_Backend.dto.response.MenuItemResponse;
import com.restaurant.Restaurant_Backend.model.DietaryTag;
import com.restaurant.Restaurant_Backend.service.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PostMapping
    public ResponseEntity<ApiResponse<MenuItemResponse>> create(
            @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Menu item created.", menuItemService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Menu item updated.",
                menuItemService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        menuItemService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Menu item deleted.", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(menuItemService.findById(id)));
    }

    // Admin: all items including unavailable ones
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(menuItemService.findAll()));
    }

    // Customer-facing: available items only
    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getAvailable() {
        return ResponseEntity.ok(ApiResponse.ok(menuItemService.findAllAvailable()));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getByCategory(
            @PathVariable String category) {
        return ResponseEntity.ok(ApiResponse.ok(menuItemService.findByCategory(category)));
    }

    @GetMapping("/dietary/{tag}")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getByDietaryTag(
            @PathVariable DietaryTag tag) {
        return ResponseEntity.ok(ApiResponse.ok(menuItemService.findByDietaryTag(tag)));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<String>>> getCategories() {
        return ResponseEntity.ok(ApiResponse.ok(menuItemService.findAllCategories()));
    }

    @PatchMapping("/{id}/toggle-availability")
    public ResponseEntity<ApiResponse<MenuItemResponse>> toggleAvailability(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Availability updated.",
                menuItemService.toggleAvailability(id)));
    }
}
