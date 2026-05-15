package com.example.Bep.Viet.service.impl;

import com.example.Bep.Viet.exception.AppException;
import com.example.Bep.Viet.exception.ErrorCode;
import com.example.Bep.Viet.model.UserFollow;
import com.example.Bep.Viet.model.UserFollowId;
import com.example.Bep.Viet.model.User;
import com.example.Bep.Viet.repository.UserFollowRepository;
import com.example.Bep.Viet.repository.UserRepository;
import com.example.Bep.Viet.response.UserResponse;
import com.example.Bep.Viet.service.UserFollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFollowServiceImpl implements UserFollowService {

    private final UserFollowRepository userFollowRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public void followUser(Long followerId, Long followingId) {
        // Không cho phép tự follow chính mình
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("User cannot follow themselves");
        }

        // Tìm 2 user trong DB, throw exception nếu không tồn tại
        User follower = findUserById(followerId);
        User following = findUserById(followingId);

        // Tạo composite key từ 2 id
        UserFollowId id = new UserFollowId(followerId, followingId);

        // Kiểm tra đã follow rồi thì không cho follow lại
        if (isFollowExist(id)) {
            throw new IllegalStateException("User " + followerId + " is already following user " + followingId);
        }

        // Tạo bản ghi follow mới và lưu vào DB
        UserFollow userFollow = UserFollow.builder()
                .id(id)
                .follower(follower)
                .following(following)
                .build();

        userFollowRepository.save(userFollow);
    }


    @Override
    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        // Tạo composite key từ 2 id
        UserFollowId id = new UserFollowId(followerId, followingId);

        // Chưa follow thì không thể unfollow
        if (!isFollowExist(id)) {
            throw new IllegalStateException("User " + followerId + " is not following user " + followingId);
        }

        // Xóa bản ghi follow khỏi DB theo composite key
        userFollowRepository.deleteById(id);
    }


    @Override
    @Transactional(readOnly = true)
    public boolean isFollowing(Long followerId, Long followingId) {
        // Tạo composite key và kiểm tra tồn tại trong DB
        return isFollowExist(new UserFollowId(followerId, followingId));
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getFollowers(Long userId) {
        // Validate user tồn tại trước khi query
        findUserById(userId);

        // Tìm tất cả bản ghi có following_id = userId
        // → lấy ra follower (người đang follow user này)
        return userFollowRepository.findByFollowingId(userId)
                .stream()
                .map(userFollow -> mapToUserResponse(userFollow.getFollower()))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getFollowing(Long userId) {
        // Validate user tồn tại trước khi query
        findUserById(userId);

        // Tìm tất cả bản ghi có follower_id = userId
        // → lấy ra following (những người user này đang follow)
        return userFollowRepository.findByFollowerId(userId)
                .stream()
                .map(userFollow -> mapToUserResponse(userFollow.getFollowing()))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public long countFollowers(Long userId) {
        // Đếm số bản ghi có following_id = userId
        return userFollowRepository.countByFollowingId(userId);
    }


    @Override
    @Transactional(readOnly = true)
    public long countFollowing(Long userId) {
        // Đếm số bản ghi có follower_id = userId
        return userFollowRepository.countByFollowerId(userId);
    }

    // ==================== HELPER ====================


    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private boolean isFollowExist(UserFollowId id) {
        // existsById → SELECT COUNT(*) WHERE follower_id = ? AND following_id = ?
        return userFollowRepository.existsById(id);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .location(user.getLocation())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}