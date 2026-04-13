package com.hrms.dashboard.controller;

import com.hrms.common.response.ApiResponse;
import com.hrms.dashboard.dto.DashboardSummaryResponse;
import com.hrms.dashboard.service.DashboardService;
import com.hrms.security.permission.Permission;
import com.hrms.security.permission.PermissionService;
import com.hrms.user.entity.User;
import com.hrms.user.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final PermissionService permissionService;
    private final UserService userService;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getSummary(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());

        permissionService.check(user, Permission.DASHBOARD_VIEW);

        DashboardSummaryResponse response = dashboardService.getSummary(user);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Dashboard summary fetched", response)
        );
    }
}