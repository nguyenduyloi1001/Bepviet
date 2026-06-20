package com.example.Bep.Viet.service;

import com.example.Bep.Viet.response.ShoppingListItemResponse;
import com.example.Bep.Viet.response.ShoppingListResponse;

import java.util.List;

public interface ShoppingListService {
    ShoppingListResponse generateFromMealPlan(Long mealPlanId, Long userId);
    ShoppingListResponse getById(Long id);
    List<ShoppingListResponse> getByUserId(Long userId);
    ShoppingListItemResponse toggleCheck(Long itemId);
    public void delete(Long id, Long userId);
}
