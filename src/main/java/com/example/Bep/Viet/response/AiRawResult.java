package com.example.Bep.Viet.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AiRawResult {
    private List<AiItem> suggestions;

    @Data
    public static class AiItem {
        @JsonProperty("recipe_id")
        private Long recipeId;

        @JsonProperty("match_score")
        private Integer matchScore;

        @JsonProperty("missing_ingredients")
        private List<String> missingIngredients;

        private String reason;
    }
}