package com.example.Bep.Viet.repository;

import com.example.Bep.Viet.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserIdAndTargetIdAndTargetType(
            Long userId, Long targetId, Like.TargetType targetType);

    long countByTargetIdAndTargetType(Long targetId, Like.TargetType targetType);

    boolean existsByUserIdAndTargetIdAndTargetType(
            Long userId, Long targetId, Like.TargetType targetType);

    @Query("SELECT COUNT(l) FROM Like l WHERE " +
            "(l.targetType = com.example.Bep.Viet.model.Like.TargetType.recipe AND l.targetId IN " +
            "(SELECT r.id FROM Recipe r WHERE r.user.id = :userId)) OR " +
            "(l.targetType = com.example.Bep.Viet.model.Like.TargetType.post AND l.targetId IN " +
            "(SELECT p.id FROM Post p WHERE p.user.id = :userId))")
    long countTotalLikesByUserId(@Param("userId") Long userId);

//    @Modifying
//    @Transactional
//    @Query("DELETE FROM Like l WHERE l.post.id = :postId")
//    void deleteByPostId(Long postId);
}