package com.example.Bep.Viet.service.Imp;

import com.example.Bep.Viet.enums.TargetType;
import com.example.Bep.Viet.exception.AppException;
import com.example.Bep.Viet.exception.ErrorCode;
import com.example.Bep.Viet.model.Comment;
import com.example.Bep.Viet.model.User;
import com.example.Bep.Viet.repository.CommentRepository;
import com.example.Bep.Viet.repository.UserRepository;
import com.example.Bep.Viet.request.CommentRequest;
import com.example.Bep.Viet.response.CommentResponse;
import com.example.Bep.Viet.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    @Override
    public CommentResponse create(CommentRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Comment parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        }

        Comment comment = Comment.builder()
                .user(user)
                .targetId(request.getTargetId())
                .targetType(request.getTargetType())
                .parent(parent)
                .content(request.getContent())
                .build();

        return mapToResponse(commentRepository.save(comment));
    }

    @Override
    public List<CommentResponse> getByTarget(Long targetId, TargetType targetType) {
        return commentRepository
                .findByTargetIdAndTargetTypeAndParentIsNull(targetId, targetType)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CommentResponse update(Long id, String content, Long currentUserId) {
        Comment comment = findById(id);

        if (!comment.getUser().getId().equals(currentUserId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        comment.setContent(content);
        return mapToResponse(commentRepository.save(comment));
    }

    @Override
    public void delete(Long id, Long currentUserId) {
        Comment comment = findById(id);

        if (!comment.getUser().getId().equals(currentUserId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        commentRepository.delete(comment);
    }

    private Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .userName(comment.getUser().getUsername())
                .userAvatar(comment.getUser().getAvatarUrl())
                .targetId(comment.getTargetId())
                .targetType(comment.getTargetType())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .content(comment.getContent())
                .replies(comment.getReplies().stream().map(this::mapToResponse).toList()) // đệ quy
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
