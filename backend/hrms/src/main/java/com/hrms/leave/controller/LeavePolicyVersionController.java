package com.hrms.leave.controller;

import com.hrms.common.response.ApiResponse;
import com.hrms.leave.dto.CreateLeavePolicyVersionRequest;
import com.hrms.leave.dto.LeavePolicyVersionResponse;
import com.hrms.leave.service.LeavePolicyVersionService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leave-policy-versions")
@RequiredArgsConstructor
public class LeavePolicyVersionController {

    private final LeavePolicyVersionService service;

    /* CREATE POLICY VERSION */

    @PostMapping
    public ApiResponse<LeavePolicyVersionResponse> createVersion(
            @RequestBody CreateLeavePolicyVersionRequest request
    ) {

        LeavePolicyVersionResponse response =
                service.createVersion(request);

        return new ApiResponse<>(
                true,
                "Policy version created successfully",
                response
        );
    }
    
    
}