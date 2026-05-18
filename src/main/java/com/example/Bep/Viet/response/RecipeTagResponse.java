package com.example.Bep.Viet.response;

import lombok.*;

@Data @Builder
public class RecipeTagResponse {
    private Long recipeId;
    private Long tagId;
}