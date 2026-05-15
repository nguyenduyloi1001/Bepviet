package com.example.Bep.Viet.service;

import com.example.Bep.Viet.enums.TargetType;
import com.example.Bep.Viet.request.CommentRequest;
import com.example.Bep.Viet.response.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse create(CommentRequest request,Long userId);
    List<CommentResponse> getByTarget(Long targetId, TargetType targetType);
    CommentResponse update(Long id, String content, Long currentUserId);
    void delete(Long id,Long currentUserId);

}
