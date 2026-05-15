package com.example.Bep.Viet.repository;

import com.example.Bep.Viet.enums.CategoryType;
import com.example.Bep.Viet.model.Category;
import com.example.Bep.Viet.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsBySlug(String slug);
    List<Category> findByType(CategoryType type);
    List<Category> findByIsActive(Boolean isActive);
    List<Category> findByTypeAndIsActive(CategoryType type, Boolean isActive);
    Optional<Category> findBySlug(String slug);

}
