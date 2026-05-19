package com.example.Bep.Viet.service;

import com.example.Bep.Viet.request.RatingRequest;
import com.example.Bep.Viet.response.RatingResponse;

import java.util.List;

public interface RatingService {
    RatingResponse rate(RatingRequest request,Long userId);
    List<RatingResponse> getByRecipeId(Long recipeId);
    Double getAverageStars(Long recipeId);
    long countByRecipeId(Long recipeId);
    void delete(Long recipeId,Long userId);
}
