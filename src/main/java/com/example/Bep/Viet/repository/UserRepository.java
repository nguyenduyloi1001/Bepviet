package com.example.Bep.Viet.repository;

import com.example.Bep.Viet.enums.Role;
import com.example.Bep.Viet.enums.UserStatus;
import com.example.Bep.Viet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    // check email tồn tại chưa — dùng khi đăng ký
    boolean existsByEmail(String email);

    // lấy theo status — dùng cho admin
    List<User> findByStatus(UserStatus status);

    // lấy theo role
    List<User> findByRole(Role role);

    // tìm kiếm theo tên — dùng cho search
    List<User> findByFullNameContainingIgnoreCase(String fullName);

    // lấy theo status + role kết hợp
    List<User> findByStatusAndRole(UserStatus status, Role role);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
