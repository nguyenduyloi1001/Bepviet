package com.example.Bep.Viet.response;

import com.example.Bep.Viet.enums.NotificationTargetType;
import com.example.Bep.Viet.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {
    private Long id;
    private Long userId;
    private Long actorId;
    private NotificationType type;
    private Long targetId;
    private NotificationTargetType targetType;
    private Boolean isRead;
    private String message;
    private LocalDateTime createdAt;
}
