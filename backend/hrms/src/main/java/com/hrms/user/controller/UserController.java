package com.hrms.user.controller;

import com.hrms.common.response.ApiResponse;
import com.hrms.security.permission.Permission;
import com.hrms.security.permission.PermissionService;
import com.hrms.user.dto.*;
import com.hrms.user.entity.User;
import com.hrms.user.service.UserService;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PermissionService permissionService;

    public UserController(UserService userService, PermissionService permissionService) {
        this.userService = userService;
        this.permissionService = permissionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.USER_CREATE);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "User created", userService.createUser(request)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.USER_UPDATE);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "User updated", userService.updateUser(id, request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> listUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.USER_VIEW_ALL);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Users fetched",
                        userService.getUsers(search, role, status, page, limit)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.USER_VIEW);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "User fetched", userService.getUserById(id)));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<ApiResponse<Void>> changeRole(
            @PathVariable Long id,
            @Valid @RequestBody ChangeRoleRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.ROLE_CHANGE);

        userService.changeRole(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Role updated", null));
    }

    // ✅ FIXED
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        User user = userService.findByEmail(userDetails.getUsername());

        // ✅ permission-based check
        permissionService.check(user, Permission.USER_CHANGE_STATUS);

        Boolean isActive = body.get("isActive");

        userService.updateUserStatus(id, isActive);

        return ResponseEntity.ok().build();
    }
}