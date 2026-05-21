package com.example.Bep.Viet.service;

import com.example.Bep.Viet.enums.NotificationTargetType;
import com.example.Bep.Viet.enums.NotificationType;
import com.example.Bep.Viet.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    List<NotificationResponse> getMyNotifications();
    List<NotificationResponse> getMyUnread();
    long countUnread();

    NotificationResponse markOneAsRead(Long notificationId);
    void markAllAsRead();

    void send(Long receiverId,
              Long actorId,
              NotificationType type,
              Long targetId,
              NotificationTargetType targetType);
}
