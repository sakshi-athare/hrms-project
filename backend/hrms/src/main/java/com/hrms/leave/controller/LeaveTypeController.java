package com.hrms.leave.controller;

import com.hrms.common.response.ApiResponse;
import com.hrms.leave.dto.CreateLeaveTypeRequest;
import com.hrms.leave.dto.LeaveTypeResponse;
import com.hrms.leave.service.LeaveTypeService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-types")
@RequiredArgsConstructor
public class LeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    /* CREATE LEAVE TYPE */

    @PostMapping
    public ApiResponse<LeaveTypeResponse> createLeaveType(
            @RequestBody CreateLeaveTypeRequest request
    ) {

        LeaveTypeResponse response =
                leaveTypeService.createLeaveType(request);

        return new ApiResponse<>(
                true,
                "Leave type created successfully",
                response
        );
    }

    /* GET ALL LEAVE TYPES */

    @GetMapping
    public ApiResponse<List<LeaveTypeResponse>> getLeaveTypes() {

        List<LeaveTypeResponse> types =
                leaveTypeService.getLeaveTypes();

        return new ApiResponse<>(
                true,
                "Leave types fetched successfully",
                types
        );
    }
}