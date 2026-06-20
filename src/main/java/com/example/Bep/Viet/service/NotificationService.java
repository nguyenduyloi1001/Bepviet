package com.example.Bep.Viet.service;

import com.example.Bep.Viet.enums.NotificationTargetType;
import com.example.Bep.Viet.enums.NotificationType;
import com.example.Bep.Viet.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    List<NotificationResponse> getMyNotifications(Long userId);

    List<NotificationResponse> getMyUnread(Long userId);

    long countUnread(Long userId);

    NotificationResponse markOneAsRead(Long notificationId, Long currentUserId);

    void markAllAsRead(Long userId);

    NotificationResponse send(Long receiverId,
                              Long actorId,
                              NotificationType type,
                              Long targetId,
                              NotificationTargetType targetType);
NotificationResponse send(Long receiverId,
                          Long actorId,
                          NotificationType type,
                          Long targetId,
                          NotificationTargetType targetType,
                          String note);
}