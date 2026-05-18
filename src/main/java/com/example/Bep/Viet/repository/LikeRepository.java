package com.example.Bep.Viet.repository;

import com.example.Bep.Viet.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserIdAndTargetIdAndTargetType(
            Long userId, Long targetId, Like.TargetType targetType);

    long countByTargetIdAndTargetType(Long targetId, Like.TargetType targetType);

    boolean existsByUserIdAndTargetIdAndTargetType(
            Long userId, Long targetId, Like.TargetType targetType);
}