package com.example.Bep.Viet.response;

import com.example.Bep.Viet.enums.DayOfWeek;
import com.example.Bep.Viet.enums.MealTime;
import lombok.Builder;
import lombok.Data;
@Data
@Builder

public class MealPlanItemResponse {
    private Long id;
    private Long recipeId;
    private String recipeName;
    private String recipeImageUrl;
    private DayOfWeek dayOfWeek;
    private MealTime mealTime;
}
