package com.example.Bep.Viet.model;

import com.example.Bep.Viet.enums.Role;
import com.example.Bep.Viet.enums.RoleRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "role_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role roleType;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RoleRequestStatus status = RoleRequestStatus.PENDING;

    @Column(length = 255)
    private String note;

    @Column(length = 500)
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
