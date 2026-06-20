package com.example.Bep.Viet.service;

import com.example.Bep.Viet.model.Recipe;
import com.example.Bep.Viet.request.IngredientRequest;
import com.example.Bep.Viet.request.RecipeIngredientRequest;
import com.example.Bep.Viet.response.RecipeIngredientResponse;

import java.util.List;

public interface RecipeIngredientService {
    void addAll(Recipe recipe, List<RecipeIngredientRequest> recipeIngredientRequests);
    RecipeIngredientResponse add(Long recipeId,RecipeIngredientRequest request);
    RecipeIngredientResponse update(Long id, RecipeIngredientRequest request);
    void delete(Long id);
    void deleteByRecipeId(Long recipeId);


}
