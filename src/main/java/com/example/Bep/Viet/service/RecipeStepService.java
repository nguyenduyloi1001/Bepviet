package com.example.Bep.Viet.service;

import com.example.Bep.Viet.model.Recipe;
import com.example.Bep.Viet.request.RecipeStepRequest;
import com.example.Bep.Viet.response.RecipeStepResponse;

import java.util.List;

public interface RecipeStepService {
    void addAll(Recipe recipe, List<RecipeStepRequest> requests);
    RecipeStepResponse add(Long recipeId, RecipeStepRequest request);
    RecipeStepResponse update(Long id, RecipeStepRequest request);
    void delete(Long id);
    List<RecipeStepResponse> getByRecipeId(Long recipeId);
    void deleteByRecipeId(Long recipeId);
}
