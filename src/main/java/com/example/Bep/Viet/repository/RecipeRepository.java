package com.example.Bep.Viet.repository;

import com.example.Bep.Viet.enums.RecipeStatus;
import com.example.Bep.Viet.model.Recipe;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe,Long> {
    Optional<Recipe> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsByName(String name);

    boolean existsByNameAndUserId(String name, Long userId);

    boolean existsByNameAndUserIdAndIdNot(String name, Long userId, Long id);

    boolean existsBySlugAndIdNot(String slug, Long id);

    List<Recipe> findByStatus(RecipeStatus status);

    List<Recipe> findByUserId(Long userId);

    List<Recipe> findByUserIdAndStatus(Long userId,RecipeStatus status);
    @Query("""
        SELECT r FROM Recipe r
        WHERE (:keyword IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) 
               OR LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:dishTypeId IS NULL OR r.dishType.id = :dishTypeId)
          AND (:regionId IS NULL OR r.region.id = :regionId)
          AND (:difficultyId IS NULL OR r.difficulty.id = :difficultyId)
          AND r.status = 'PUBLISHED'
        """)
    List<Recipe> searchRecipes(
            @Param("keyword") String keyword,
            @Param("dishTypeId") Long dishTypeId,
            @Param("regionId") Long regionId,
            @Param("difficultyId") Long difficultyId);

    @Modifying
    @Query("UPDATE Recipe r SET r.viewCount = r.viewCount + 1 WHERE r.id = :id")
    void incrementViewCount(@Param("id") Long id);

}
