package com.example.Bep.Viet.service;

import com.example.Bep.Viet.enums.CategoryType;
import com.example.Bep.Viet.request.CategoryRequest;
import com.example.Bep.Viet.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse getCategoryById(Long id);
    CategoryResponse getCategoryBySlug(String slug);
    List<CategoryResponse> getAllCategory();
    List<CategoryResponse> getAllCategoryByType(CategoryType type);
    List<CategoryResponse> getAllCategoryByStatus(Boolean isActive);
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    CategoryResponse toggleActive(Long id);
    void deleteCategory(Long id);
}
