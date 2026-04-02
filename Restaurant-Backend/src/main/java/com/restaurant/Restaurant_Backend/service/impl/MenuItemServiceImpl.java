package com.restaurant.Restaurant_Backend.service.impl;


import com.restaurant.dto.request.MenuItemRequest;
import com.restaurant.dto.response.MenuItemResponse;
import com.restaurant.exception.ResourceNotFoundException;
import com.restaurant.model.DietaryTag;
import com.restaurant.model.MenuItem;
import com.restaurant.repository.MenuItemRepository;
import com.restaurant.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;

    @Override
    public MenuItemResponse create(MenuItemRequest request) {
        MenuItem item = toEntity(new MenuItem(), request);
        return toResponse(menuItemRepository.save(item));
    }

    @Override
    public MenuItemResponse update(Long id, MenuItemRequest request) {
        MenuItem item = findItemById(id);
        toEntity(item, request);
        return toResponse(menuItemRepository.save(item));
    }

    @Override
    public void delete(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("MenuItem", id);
        }
        menuItemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemResponse findById(Long id) {
        return toResponse(findItemById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> findAll() {
        return menuItemRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> findAllAvailable() {
        return menuItemRepository.findByIsAvailableTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> findByCategory(String category) {
        return menuItemRepository
                .findByCategoryIgnoreCaseAndIsAvailableTrue(category)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> findByDietaryTag(DietaryTag tag) {
        return menuItemRepository.findAvailableByDietaryTag(tag).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findAllCategories() {
        return menuItemRepository.findAllCategories();
    }

    @Override
    public MenuItemResponse toggleAvailability(Long id) {
        MenuItem item = findItemById(id);
        item.setIsAvailable(!item.getIsAvailable());
        return toResponse(menuItemRepository.save(item));
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private MenuItem findItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", id));
    }

    private MenuItem toEntity(MenuItem item, MenuItemRequest req) {
        item.setName(req.getName());
        item.setDescription(req.getDescription());
        item.setPrice(req.getPrice());
        item.setCategory(req.getCategory());
        item.setImageEmoji(req.getImageEmoji());
        item.setImageUrl(req.getImageUrl());
        item.setPrepTimeMinutes(req.getPrepTimeMinutes() != null ? req.getPrepTimeMinutes() : 10);
        item.setIsSpicy(req.getIsSpicy() != null ? req.getIsSpicy() : false);
        item.setIsAvailable(req.getIsAvailable() != null ? req.getIsAvailable() : true);
        item.setDietaryTags(req.getDietaryTags());
        item.setAllergens(req.getAllergens());
        return item;
    }

    private MenuItemResponse toResponse(MenuItem item) {
        return MenuItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .category(item.getCategory())
                .imageEmoji(item.getImageEmoji())
                .imageUrl(item.getImageUrl())
                .prepTimeMinutes(item.getPrepTimeMinutes())
                .isSpicy(item.getIsSpicy())
                .isAvailable(item.getIsAvailable())
                .dietaryTags(item.getDietaryTags())
                .allergens(item.getAllergens())
                .build();
    }
}
