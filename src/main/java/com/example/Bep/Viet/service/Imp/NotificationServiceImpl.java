package com.example.Bep.Viet.service.Imp;

import com.example.Bep.Viet.enums.NotificationTargetType;
import com.example.Bep.Viet.enums.NotificationType;
import com.example.Bep.Viet.model.Notification;
import com.example.Bep.Viet.repository.NotificationRepository;
import com.example.Bep.Viet.response.NotificationResponse;
import com.example.Bep.Viet.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    // ✅ JwtFilter set principal = userId (Long) → lấy trực tiếp
    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Long) {
            return (Long) principal;
        }
        return Long.parseLong(principal.toString());
    }

    // ─── Đọc ───────────────────────────────────────────────────

    @Override
    public List<NotificationResponse> getMyNotifications() {
        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(getCurrentUserId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponse> getMyUnread() {
        return notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(getCurrentUserId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public long countUnread() {
        return notificationRepository.countByUserIdAndIsRead(getCurrentUserId(), false);
    }

    // ─── Đánh dấu đã đọc ───────────────────────────────────────

    @Override
    public NotificationResponse markOneAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getUserId().equals(getCurrentUserId())) {
            throw new RuntimeException("Không có quyền đọc thông báo này");
        }

        notification.setIsRead(true);
        return mapToResponse(notificationRepository.save(notification));
    }

    @Override
    @Transactional
    public void markAllAsRead() {
        notificationRepository.markAllAsRead(getCurrentUserId());
    }

    // ─── Gửi thông báo (gọi nội bộ từ các service khác) ───────

    @Override
    public void send(Long receiverId,
                     Long actorId,
                     NotificationType type,
                     Long targetId,
                     NotificationTargetType targetType) {

        // Không tự gửi thông báo cho chính mình
        if (actorId != null && actorId.equals(receiverId)) {
            return;
        }

        notificationRepository.save(Notification.builder()
                .userId(receiverId)
                .actorId(actorId)
                .type(type)
                .targetId(targetId)
                .targetType(targetType)
                .build());
    }

    // ─── Mapper ────────────────────────────────────────────────

    private NotificationResponse mapToResponse(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .userId(n.getUserId())
                .actorId(n.getActorId())
                .type(n.getType())
                .targetId(n.getTargetId())
                .targetType(n.getTargetType())
                .isRead(n.getIsRead())
                .createdAt(n.getCreatedAt())
                .message(buildMessage(n))
                .build();
    }

    private String buildMessage(Notification n) {
        String actor = n.getActorId() != null
                ? "Người dùng #" + n.getActorId()
                : "Hệ thống";

        return switch (n.getType()) {
            case new_follower    -> actor + " đã bắt đầu theo dõi bạn.";
            case new_comment     -> actor + " đã bình luận vào bài viết của bạn.";
            case new_like        -> actor + " đã thích nội dung của bạn.";
            case new_answer      -> actor + " đã trả lời bình luận của bạn.";
            case recipe_approved -> "Công thức của bạn đã được duyệt!";
            case recipe_rejected -> "Công thức của bạn chưa được duyệt. Vui lòng chỉnh sửa lại.";
        };
    }
}
