package com.example.Bep.Viet.repository;

import com.example.Bep.Viet.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating,Long> {
    Optional<Rating> findByUserIdAndRecipeId(Long userId, Long recipeId);

    // Lấy tất cả rating của 1 recipe
    List<Rating> findByRecipeId(Long recipeId);

    // Tính điểm trung bình của 1 recipe
    @Query("SELECT AVG(r.stars) FROM Rating r WHERE r.recipe.id = :recipeId")
    Double getAverageStars(@Param("recipeId") Long recipeId);

    // Đếm số lượng rating của 1 recipe
    long countByRecipeId(Long recipeId);
}
