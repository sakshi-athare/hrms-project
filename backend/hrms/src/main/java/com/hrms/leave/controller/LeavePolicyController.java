package com.hrms.leave.controller;

import com.hrms.common.response.ApiResponse;
import com.hrms.leave.dto.CreateLeavePolicyRequest;
import com.hrms.leave.dto.LeavePolicyResponse;
import com.hrms.leave.service.LeavePolicyService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-policies")
@RequiredArgsConstructor
public class LeavePolicyController {

    private final LeavePolicyService leavePolicyService;

    /* CREATE POLICY */

    @PostMapping
    public ApiResponse<LeavePolicyResponse> createPolicy(
            @RequestBody CreateLeavePolicyRequest request
    ) {

        LeavePolicyResponse response =
                leavePolicyService.createPolicy(request);

        return new ApiResponse<>(
                true,
                "Leave policy created successfully",
                response
        );
    }

    /* GET ALL POLICIES */

    @GetMapping
    public ApiResponse<List<LeavePolicyResponse>> getPolicies() {

        List<LeavePolicyResponse> policies =
                leavePolicyService.getPolicies();

        return new ApiResponse<>(
                true,
                "Leave policies fetched successfully",
                policies
        );
    }
}