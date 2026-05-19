package com.example.Bep.Viet.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder

public class RatingResponse {
    private Long id;
    private Long userId;
    private String userName;
    private Long recipeId;
    private Integer stars;
    private LocalDateTime createdAt;
}
