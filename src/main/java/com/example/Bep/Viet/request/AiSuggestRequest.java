package com.example.Bep.Viet.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class AiSuggestRequest {

    @NotEmpty(message = "Vui lòng nhập ít nhất 1 nguyên liệu")
    @Size(max = 20, message = "Tối đa 20 nguyên liệu")
    private List<String> ingredients;

    private String diet;
    private Integer cookingTimeMax;
}