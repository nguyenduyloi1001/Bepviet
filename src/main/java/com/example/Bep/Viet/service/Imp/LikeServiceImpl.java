package com.example.Bep.Viet.service.Imp;

import com.example.Bep.Viet.model.Like;
import com.example.Bep.Viet.repository.LikeRepository;
import com.example.Bep.Viet.request.LikeRequest;
import com.example.Bep.Viet.response.LikeResponse;
import com.example.Bep.Viet.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Override
    @Transactional
    public LikeResponse toggle(Long userId, LikeRequest request) {
        Like.TargetType type = request.getTargetType();
        Long targetId = request.getTargetId();

        Optional<Like> existing = likeRepository
                .findByUserIdAndTargetIdAndTargetType(userId, targetId, type);

        boolean liked;
        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
            liked = false;
        } else {
            likeRepository.save(Like.builder()
                    .userId(userId)
                    .targetId(targetId)
                    .targetType(type)
                    .build());
            liked = true;
        }

        long total = likeRepository.countByTargetIdAndTargetType(targetId, type);
        return LikeResponse.builder().liked(liked).totalLikes(total).build();
    }

    @Override
    public long count(Long targetId, String targetType) {
        return likeRepository.countByTargetIdAndTargetType(
                targetId, Like.TargetType.valueOf(targetType));
    }

    @Override
    public boolean isLiked(Long userId, Long targetId, String targetType) {
        return likeRepository.existsByUserIdAndTargetIdAndTargetType(
                userId, targetId, Like.TargetType.valueOf(targetType));
    }
}