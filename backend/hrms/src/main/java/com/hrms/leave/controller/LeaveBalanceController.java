package com.hrms.leave.controller;

import com.hrms.common.response.ApiResponse;
import com.hrms.leave.service.LeaveBalanceService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leave-balances")
@RequiredArgsConstructor
public class LeaveBalanceController {

    private final LeaveBalanceService leaveBalanceService;

    @PostMapping("/generate")
    public ApiResponse<Void> generateLeaveBalances() {

        leaveBalanceService.generateAnnualLeaveBalances();

        return new ApiResponse<>(
                true,
                "Leave balances generated successfully",
                null
        );
    }
}