package com.example.Bep.Viet.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RecipeIngredientRequest {
    @NotNull(message = "Tên nguyên liệu không được để trống")
    private Long ingredientId;

    private BigDecimal quantity;

    private String unit;

    private String note;
}
