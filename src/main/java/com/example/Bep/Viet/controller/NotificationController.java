package com.example.Bep.Viet.controller;

import com.example.Bep.Viet.enums.NotificationTargetType;
import com.example.Bep.Viet.enums.NotificationType;
import com.example.Bep.Viet.response.NotificationResponse;
import com.example.Bep.Viet.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // GET /api/notifications
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotificationResponse>> getAll() {
        return ResponseEntity.ok(notificationService.getMyNotifications());
    }

    // GET /api/notifications/unread
    @GetMapping("/unread")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotificationResponse>> getUnread() {
        return ResponseEntity.ok(notificationService.getMyUnread());
    }

    // GET /api/notifications/unread/count
    @GetMapping("/unread/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> countUnread() {
        return ResponseEntity.ok(notificationService.countUnread());
    }

    // PATCH /api/notifications/{id}/read
    @PatchMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificationResponse> markOne(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markOneAsRead(id));
    }

    // PATCH /api/notifications/read-all
    @PatchMapping("/read-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> markAll() {
        notificationService.markAllAsRead();
        return ResponseEntity.noContent().build();
    }

    // ✅ TEST ONLY: Tạo thông báo thủ công để test
    // POST /api/notifications/test?receiverId=1&type=new_like&targetId=1&targetType=recipe
    @PostMapping("/test")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> sendTest(
            @RequestParam Long receiverId,
            @RequestParam String type,
            @RequestParam(required = false) Long targetId,
            @RequestParam(required = false) String targetType) {

        notificationService.send(
                receiverId,
                null,
                NotificationType.valueOf(type),
                targetId,
                targetType != null ? NotificationTargetType.valueOf(targetType) : null
        );
        return ResponseEntity.ok("Đã gửi thông báo test thành công!");
    }
}
