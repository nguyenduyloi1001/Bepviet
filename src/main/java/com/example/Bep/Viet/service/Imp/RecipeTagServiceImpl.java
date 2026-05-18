package com.example.Bep.Viet.service.Imp;

import com.example.Bep.Viet.model.RecipeTags;
import com.example.Bep.Viet.repository.RecipeTagRepository;
import com.example.Bep.Viet.request.RecipeTagRequest;
import com.example.Bep.Viet.response.RecipeTagResponse;
import com.example.Bep.Viet.service.RecipeTagService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeTagServiceImpl implements RecipeTagService {

    private final RecipeTagRepository recipeTagRepository;

    @Override
    public RecipeTagResponse assign(RecipeTagRequest request) {
        RecipeTags recipeTag = RecipeTags.builder()
                .recipeId(request.getRecipeId())
                .tagId(request.getTagId())
                .build();
        recipeTagRepository.save(recipeTag);
        return RecipeTagResponse.builder()
                .recipeId(recipeTag.getRecipeId())
                .tagId(recipeTag.getTagId())
                .build();
    }

    @Override
    public List<RecipeTagResponse> getByRecipe(Long recipeId) {
        return recipeTagRepository.findByRecipeId(recipeId)
                .stream()
                .map(rt -> RecipeTagResponse.builder()
                        .recipeId(rt.getRecipeId())
                        .tagId(rt.getTagId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void remove(Long recipeId, Long tagId) {
        recipeTagRepository.deleteByRecipeIdAndTagId(recipeId, tagId);
    }
}