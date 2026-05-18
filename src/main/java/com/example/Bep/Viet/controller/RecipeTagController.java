package com.example.Bep.Viet.controller;

import com.example.Bep.Viet.request.RecipeTagRequest;
import com.example.Bep.Viet.response.RecipeTagResponse;
import com.example.Bep.Viet.service.RecipeTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe-tags")
@RequiredArgsConstructor
public class RecipeTagController {

    private final RecipeTagService recipeTagService;

    // GET public
    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<List<RecipeTagResponse>> getByRecipe(@PathVariable Long recipeId) {
        return ResponseEntity.ok(recipeTagService.getByRecipe(recipeId));
    }

    // POST/DELETE - phải đăng nhập
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RecipeTagResponse> assign(@RequestBody RecipeTagRequest request) {
        return ResponseEntity.ok(recipeTagService.assign(request));
    }

    @DeleteMapping("/recipe/{recipeId}/tag/{tagId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> remove(@PathVariable Long recipeId, @PathVariable Long tagId) {
        recipeTagService.remove(recipeId, tagId);
        return ResponseEntity.noContent().build();
    }
}