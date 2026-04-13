package com.hrms.leave.controller;

import com.hrms.common.response.ApiResponse;
import com.hrms.leave.dto.ApplyLeaveRequest;
import com.hrms.leave.dto.LeaveBalanceResponse;
import com.hrms.leave.dto.LeaveResponse;
import com.hrms.leave.dto.RejectLeaveRequest;
import com.hrms.leave.service.LeaveService;
import com.hrms.user.entity.User;
import com.hrms.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;
    private final UserRepository userRepository;

    /* APPLY LEAVE */

    @PostMapping
    public ApiResponse<LeaveResponse> applyLeave(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ApplyLeaveRequest request
    ) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow();

        LeaveResponse response =
                leaveService.applyLeave(user.getId(), request);

        return new ApiResponse<>(
                true,
                "Leave applied successfully",
                response
        );
    }

    /* APPROVE LEAVE */

    @PatchMapping("/{id}/approve")
    public ApiResponse<LeaveResponse> approveLeave(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        User manager = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow();

        LeaveResponse response =
                leaveService.approveLeave(id, manager.getId());

        return new ApiResponse<>(
                true,
                "Leave approved successfully",
                response
        );
    }

    /* REJECT LEAVE */

    @PatchMapping("/{id}/reject")
    public ApiResponse<LeaveResponse> rejectLeave(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody RejectLeaveRequest request
    ) {

        User manager = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow();

        LeaveResponse response =
                leaveService.rejectLeave(id, manager.getId(), request.getReason());

        return new ApiResponse<>(
                true,
                "Leave rejected successfully",
                response
        );
    }

    /* CANCEL LEAVE */

    @PatchMapping("/{id}/cancel")
    public ApiResponse<LeaveResponse> cancelLeave(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow();

        LeaveResponse response =
                leaveService.cancelLeave(id, user.getId());

        return new ApiResponse<>(
                true,
                "Leave cancelled successfully",
                response
        );
    }

    /* MY LEAVES */

    @GetMapping("/my")
    public ApiResponse<List<LeaveResponse>> getMyLeaves(
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow();

        List<LeaveResponse> leaves =
                leaveService.getMyLeaves(user.getId());

        return new ApiResponse<>(
                true,
                "My leaves fetched successfully",
                leaves
        );
    }

    /* PENDING LEAVES (Manager) */

    @GetMapping("/pending")
    public ApiResponse<List<LeaveResponse>> getPendingLeaves(
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        User manager = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow();

        List<LeaveResponse> leaves =
                leaveService.getPendingLeaves(manager.getId());

        return new ApiResponse<>(
                true,
                "Pending leaves fetched successfully",
                leaves
        );
    }

    /* LEAVE BALANCE */

    @GetMapping("/balance")
    public ApiResponse<LeaveBalanceResponse> getLeaveBalance(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long leaveTypeId
    ) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow();

        LeaveBalanceResponse balance =
                leaveService.getLeaveBalance(user.getId(), leaveTypeId);

        return new ApiResponse<>(
                true,
                "Leave balance fetched successfully",
                balance
        );
    }
}