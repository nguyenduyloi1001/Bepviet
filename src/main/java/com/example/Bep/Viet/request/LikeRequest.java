package com.example.Bep.Viet.request;

import com.example.Bep.Viet.model.Like;
import lombok.Data;

@Data
public class LikeRequest {
    private Long targetId;
    private Like.TargetType targetType;
}