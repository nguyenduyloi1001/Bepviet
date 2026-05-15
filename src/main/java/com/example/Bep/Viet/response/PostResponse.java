package com.example.Bep.Viet.response;

import com.example.Bep.Viet.enums.PostStatus;
import com.example.Bep.Viet.enums.PostType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String content;
    private String thumbnail;
    private String slug;
    private PostType type;
    private PostStatus status;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
