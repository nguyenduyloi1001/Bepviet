package com.example.Bep.Viet.service;

import com.example.Bep.Viet.enums.RecipeStatus;
import com.example.Bep.Viet.request.RecipeRequest;
import com.example.Bep.Viet.response.RecipeResponse;
import com.example.Bep.Viet.response.UserResponse;

import java.util.List;

public interface RecipeService {
    RecipeResponse createRecipe(RecipeRequest request,Long userId);

    RecipeResponse getRecipeById(Long id);

    RecipeResponse getBySlug(String slug);

    List<RecipeResponse> getAllRecipe();

    List<RecipeResponse> search(String keyword,Long dishTypeId,Long regionId,Long difficultyId);

    RecipeResponse updateRecipe(Long id,RecipeRequest request,Long currentUserId);

    void delete(Long id,Long currentUserId);

    RecipeResponse approve(Long id);

    RecipeResponse reject(Long id);

    List<RecipeResponse> getByUserId(Long userId);

    List<RecipeResponse> getByStatus(RecipeStatus status);
}
