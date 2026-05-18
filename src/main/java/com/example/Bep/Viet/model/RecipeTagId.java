package com.example.Bep.Viet.model;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class RecipeTagId implements Serializable {
    private Long recipeId;
    private Long tagId;
}