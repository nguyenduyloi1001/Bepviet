package com.example.Bep.Viet.service;

import com.example.Bep.Viet.request.RecipeTagRequest;
import com.example.Bep.Viet.response.RecipeTagResponse;

import java.util.List;

public interface RecipeTagService {
    RecipeTagResponse assign(RecipeTagRequest request);
    List<RecipeTagResponse> getByRecipe(Long recipeId);
    void remove(Long recipeId, Long tagId);
}