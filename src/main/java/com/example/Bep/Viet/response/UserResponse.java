package com.example.Bep.Viet.response;

import com.example.Bep.Viet.enums.Role;
import com.example.Bep.Viet.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String username;
    private String fullName;
    private String avatarUrl;
    private String bio;
    private String location;
    private Role role;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
