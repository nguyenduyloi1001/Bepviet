package com.example.Bep.Viet.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class MealPlanRequest {
    private String name;

    @NotNull(message = "Week start date is required")
    private LocalDate weekStartDate;
}
