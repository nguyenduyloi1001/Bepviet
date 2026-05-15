package com.example.Bep.Viet.service;

import com.example.Bep.Viet.model.User;
import com.example.Bep.Viet.response.UserResponse;

import java.util.List;

public interface UserFollowService {
    void followUser(Long followerId,Long followingId);
    void unfollowUser (Long followId,Long followingId);
    boolean isFollowing(Long followId,Long followingId);
    List<UserResponse> getFollowers(Long userId);
    List<UserResponse> getFollowing(Long userId);
    long countFollowers(Long userId);
    long countFollowing(Long userId);
}
