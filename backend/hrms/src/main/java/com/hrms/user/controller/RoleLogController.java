package com.hrms.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.common.response.ApiResponse;
import com.hrms.security.permission.Permission;
import com.hrms.security.permission.PermissionService;
import com.hrms.user.entity.User;
import com.hrms.user.service.UserService;

@RestController
@RequestMapping("/api/role-logs")
public class RoleLogController {

    private final UserService userService;
    private final PermissionService permissionService;

    public RoleLogController(
            UserService userService,
            PermissionService permissionService) {
        this.userService = userService;
        this.permissionService = permissionService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getRoleLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = userService.findByEmail(userDetails.getUsername());
        permissionService.check(currentUser, Permission.ROLE_LOG_VIEW);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Role logs fetched",
                        userService.getRoleLogs(page, limit)
                )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<?>> getLogsByUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = userService.findByEmail(userDetails.getUsername());
        permissionService.check(currentUser, Permission.ROLE_LOG_VIEW);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "User role logs fetched",
                        userService.getLogsByUser(userId)
                )
        );
    }
}


