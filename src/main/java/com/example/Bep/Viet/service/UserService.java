package com.example.Bep.Viet.service;

import com.example.Bep.Viet.enums.Role;
import com.example.Bep.Viet.enums.UserStatus;
import com.example.Bep.Viet.request.UserRequest;
import com.example.Bep.Viet.request.UserUpdateRequest;
import com.example.Bep.Viet.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest request);
    UserResponse getUserById(Long userId);
    List<UserResponse> getAllUser();
    List<UserResponse> getAllUserByStatus(UserStatus status);
    List<UserResponse> getAllUserByRole(Role role);
    UserResponse updateUser(Long userId,UserUpdateRequest request);
    UserResponse updateStatusUser(Long userId,UserStatus status);
    void deleteUserById(Long id);
}
