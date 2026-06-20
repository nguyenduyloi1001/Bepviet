package com.example.Bep.Viet.service;

import com.example.Bep.Viet.request.LikeRequest;
import com.example.Bep.Viet.response.LikeResponse;

public interface LikeService {
    LikeResponse toggle(Long userId, LikeRequest request);
    long count(Long targetId, String targetType);
    boolean isLiked(Long userId, Long targetId, String targetType);
    long countTotalLikesByUserId(Long userId);
}