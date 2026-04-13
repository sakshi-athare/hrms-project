package com.hrms.user.service;

import com.hrms.user.dto.*;
import com.hrms.user.entity.User;

import java.util.Map;

public interface UserService {

    /* ================= CREATE ================= */
    UserResponse createUser(CreateUserRequest request);

    /* ================= UPDATE ================= */
    UserResponse updateUser(Long userId, UpdateUserRequest request);

    /* ================= STATUS ================= */
    void updateUserStatus(Long userId, Boolean isActive);

    void deactivateUser(Long userId);

    /* ================= ROLE ================= */
    void changeRole(Long userId, ChangeRoleRequest request);

    Map<String, Object> getRoleLogs(int page, int limit);

    /* ================= GET ================= */
    UserResponse getUserById(Long id);

    
    public Map<String, Object> getUsers(String search, String role, Boolean status, int page, int limit);
    /* ================= CURRENT USER ================= */
    UserResponse getCurrentUserProfile();

    /* ================= INTERNAL ================= */
    User findByEmail(String email);

	Object getLogsByUser(Long userId);
}