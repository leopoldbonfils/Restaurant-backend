package com.restaurant.Restaurant_Backend.service.impl;

import com.restaurant.Restaurant_Backend.dto.request.MenuItemRequest;
import com.restaurant.Restaurant_Backend.dto.response.MenuItemResponse;
import com.restaurant.Restaurant_Backend.exception.ResourceNotFoundException;
import com.restaurant.Restaurant_Backend.model.DietaryTag;
import com.restaurant.Restaurant_Backend.model.MenuItem;
import com.restaurant.Restaurant_Backend.repository.MenuItemRepository;
import com.restaurant.Restaurant_Backend.service.MenuItemService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    @SuppressWarnings("null")
    public MenuItemResponse create(MenuItemRequest request) {
        MenuItem item = toEntity(new MenuItem(), request);
        MenuItem saved = menuItemRepository.save(item);
        return toResponse(saved);
    }

    @Override
    @SuppressWarnings("null")
    public MenuItemResponse update(Long id, MenuItemRequest request) {
        Long nonNullId = Objects.requireNonNull(id);
        MenuItem item = findItemById(nonNullId);
        toEntity(item, request);
        MenuItem saved = menuItemRepository.save(item);
        return toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Long nonNullId = Objects.requireNonNull(id);
        if (!menuItemRepository.existsById(nonNullId)) {
            throw new ResourceNotFoundException("MenuItem", nonNullId);
        }
        menuItemRepository.deleteById(nonNullId);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemResponse findById(Long id) {
        Long nonNullId = Objects.requireNonNull(id);
        return toResponse(findItemById(nonNullId));
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
    @SuppressWarnings("null")
    public MenuItemResponse toggleAvailability(Long id) {
        Long nonNullId = Objects.requireNonNull(id);
        MenuItem item = findItemById(nonNullId);
        item.setIsAvailable(!item.getIsAvailable());
        MenuItem saved = menuItemRepository.save(item);
        return toResponse(saved);
    }

    private MenuItem findItemById(@NonNull Long id) {
        Objects.requireNonNull(id);
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
        Integer prepTime = req.getPrepTimeMinutes();
        Boolean isSpicy = req.getIsSpicy();
        Boolean isAvailable = req.getIsAvailable();

        item.setPrepTimeMinutes(prepTime != null ? prepTime : 10);
        item.setIsSpicy(isSpicy != null ? isSpicy : false);
        item.setIsAvailable(isAvailable != null ? isAvailable : true);
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