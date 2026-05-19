package com.example.Bep.Viet.request;

import com.example.Bep.Viet.enums.DayOfWeek;
import com.example.Bep.Viet.enums.MealTime;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MealPlanItemRequest {
    @NotNull
    private Long recipeId;

    @NotNull
    private DayOfWeek dayOfWeek;

    @NotNull
    private MealTime mealTime;
}
