package com.example.Bep.Viet.repository;

import com.example.Bep.Viet.model.RecipeTags;
import com.example.Bep.Viet.model.RecipeTagId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeTagRepository extends JpaRepository<RecipeTags, RecipeTagId> {
    List<RecipeTags> findByRecipeId(Long recipeId);
    List<RecipeTags> findByTagId(Long tagId);
    void deleteByRecipeIdAndTagId(Long recipeId, Long tagId);
}