package com.example.Bep.Viet.request;

import lombok.Data;

@Data
public class RecipeTagRequest {
    private Long recipeId;
    private Long tagId;
}