package com.hrms.attendance.controller;

import com.hrms.attendance.dto.*;
import com.hrms.attendance.service.AttendanceService;
import com.hrms.common.response.ApiResponse;
import com.hrms.security.permission.Permission;
import com.hrms.security.permission.PermissionService;
import com.hrms.user.entity.User;
import com.hrms.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final UserService userService;
    private final PermissionService permissionService;

    // =============================
    // CHECK-IN
    // =============================
    @PostMapping("/check-in")
    public ResponseEntity<ApiResponse<AttendanceResponse>> checkIn(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.ATTENDANCE_CHECK_IN);

        AttendanceResponse attendance = attendanceService.checkIn(user.getEmail());

        return ResponseEntity.ok(new ApiResponse<>(true, "Check-in successful", attendance));
    }

    // =============================
    // CHECK-OUT
    // =============================
    @PostMapping("/check-out")
    public ResponseEntity<ApiResponse<AttendanceResponse>> checkOut(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.ATTENDANCE_CHECK_OUT);

        AttendanceResponse attendance = attendanceService.checkOut(user.getEmail());

        return ResponseEntity.ok(new ApiResponse<>(true, "Check-out successful", attendance));
    }

    // =============================
    // TODAY
    // =============================
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<TodayAttendanceResponse>> today(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.ATTENDANCE_VIEW_SELF);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Today attendance",
                        attendanceService.getTodayAttendance(user.getEmail()))
        );
    }

    // =============================
    // HISTORY
    // =============================
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<?>> history(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());

        if (permissionService.has(user, Permission.ATTENDANCE_VIEW_ALL)) {
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "All attendance",
                            attendanceService.getAllAttendance()) // 👉 you must implement this
            );
        }

        permissionService.check(user, Permission.ATTENDANCE_VIEW_SELF);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "My attendance",
                        attendanceService.getAttendanceHistory(user))
        );
    }

    // =============================
    // MANUAL ENTRY (HR)
    // =============================
    @PostMapping("/manual")
    public ResponseEntity<ApiResponse<AttendanceResponse>> manual(
            @Valid @RequestBody ManualAttendanceRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.ATTENDANCE_MANUAL_CREATE);

        AttendanceResponse attendance =
                attendanceService.createManualAttendance(request, user.getEmail());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Manual attendance created", attendance)
        );
    }

    // =============================
    // UPDATE
    // =============================
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<AttendanceResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAttendanceRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.ATTENDANCE_UPDATE);

        AttendanceResponse updated =
                attendanceService.updateAttendance(id, request, user.getEmail());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Attendance updated", updated)
        );
    }

    // =============================
    // REGULARIZATION REQUEST
    // =============================
    @PostMapping("/regularization/request")
    public ResponseEntity<ApiResponse<?>> requestRegularization(
            @Valid @RequestBody RegularizationRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.ATTENDANCE_REGULARIZE);

        attendanceService.requestRegularization(request, user.getEmail());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Regularization requested", null)
        );
    }

    // =============================
    // APPROVE
    // =============================
    @PostMapping("/regularization/{id}/approve")
    public ResponseEntity<ApiResponse<?>> approve(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.ATTENDANCE_UPDATE);

        attendanceService.approveRegularization(id, user.getEmail());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Approved", null)
        );
    }

    // =============================
    // REJECT
    // =============================
    @PostMapping("/regularization/{id}/reject")
    public ResponseEntity<ApiResponse<?>> reject(
            @PathVariable Long id,
            @RequestBody RejectRegularizationRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.ATTENDANCE_UPDATE);

        attendanceService.rejectRegularization(id, request.getReason(), user.getEmail());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Rejected", null)
        );
    }

    // =============================
    // PENDING (HR)
    // =============================
    @GetMapping("/regularization/pending")
    public ResponseEntity<ApiResponse<List<RegularizationResponse>>> pending(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.ATTENDANCE_VIEW_ALL);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Pending requests",
                        attendanceService.getPendingRegularizations())
        );
    }
    // =============================
    // CALENDAR
    // =============================
    @GetMapping("/calendar")
    public ResponseEntity<ApiResponse<List<AttendanceCalendarResponse>>> calendar(
            @RequestParam int year,
            @RequestParam int month,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.ATTENDANCE_VIEW_SELF);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Calendar",
                        attendanceService.getAttendanceCalendar(
                                user.getEmail(), year, month))
        );
    }
    
    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<Page<AttendanceResponse>>> filter(
            @RequestBody AttendanceFilterRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.ATTENDANCE_VIEW_ALL);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Filtered attendance",
                        attendanceService.getFilteredAttendance(request))
        );
    }
}