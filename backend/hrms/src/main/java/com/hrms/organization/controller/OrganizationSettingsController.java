package com.hrms.organization.controller;

import com.hrms.common.response.ApiResponse;
import com.hrms.organization.service.OrganizationSettingsService;
import com.hrms.security.permission.Permission;
import com.hrms.security.permission.PermissionService;
import com.hrms.user.entity.User;
import com.hrms.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.hrms.organization.dto.OrganizationSettingsRequest;
import com.hrms.organization.dto.OrganizationSettingsResponse;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class OrganizationSettingsController {

	private final OrganizationSettingsService service;
	private final UserService userService;
	private final PermissionService permissionService;

	@PostMapping
	public ResponseEntity<ApiResponse<OrganizationSettingsResponse>> createSettings(
			@Valid @RequestBody OrganizationSettingsRequest request, @AuthenticationPrincipal UserDetails userDetails) {

		User user = userService.findByEmail(userDetails.getUsername());
		permissionService.check(user, Permission.SETTINGS_UPDATE);

		return ResponseEntity.ok(new ApiResponse<>(true, "Settings created", service.createSettings(request)));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<OrganizationSettingsResponse>> getSettings(
			@AuthenticationPrincipal UserDetails userDetails) {

		User user = userService.findByEmail(userDetails.getUsername());
		permissionService.check(user, Permission.SETTINGS_VIEW);

		return ResponseEntity.ok(new ApiResponse<>(true, "Settings fetched", service.getSettings()));
	}

	@PutMapping
	public ResponseEntity<ApiResponse<OrganizationSettingsResponse>> updateSettings(
			@Valid @RequestBody OrganizationSettingsRequest request, @AuthenticationPrincipal UserDetails userDetails) {

		User user = userService.findByEmail(userDetails.getUsername());
		permissionService.check(user, Permission.SETTINGS_UPDATE);

		return ResponseEntity.ok(new ApiResponse<>(true, "Settings updated", service.updateSettings(request)));
	}
}