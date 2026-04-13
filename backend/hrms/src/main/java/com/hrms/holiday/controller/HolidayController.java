package com.hrms.holiday.controller;

import com.hrms.common.exception.BadRequestException;
import com.hrms.common.response.ApiResponse;
import com.hrms.holiday.dto.HolidayRequest;
import com.hrms.holiday.dto.HolidayResponse;
import com.hrms.holiday.service.HolidayService;
import com.hrms.security.permission.Permission;
import com.hrms.security.permission.PermissionService;
import com.hrms.user.entity.User;
import com.hrms.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/holidays")
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayService holidayService;
    private final UserService userService;
    private final PermissionService permissionService;

    // CREATE
    @PostMapping
    public ResponseEntity<ApiResponse<HolidayResponse>> createHoliday(
            @Valid @RequestBody HolidayRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.HOLIDAY_CREATE);

        HolidayResponse response = holidayService.createHoliday(request, user);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Holiday created", response)
        );
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<ApiResponse<List<HolidayResponse>>> getAllHolidays(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.HOLIDAY_VIEW);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Holiday list",
                        holidayService.getAllHolidays())
        );
    }

    // BY YEAR
    @GetMapping("/year/{year}")
    public ResponseEntity<ApiResponse<List<HolidayResponse>>> getByYear(
            @PathVariable Integer year,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (year < 2000 || year > LocalDate.now().plusYears(2).getYear()) {
            throw new BadRequestException("Invalid year");
        }

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.HOLIDAY_VIEW);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Holiday list",
                        holidayService.getHolidaysByYear(year))
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HolidayResponse>> updateHoliday(
            @PathVariable Long id,
            @Valid @RequestBody HolidayRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.HOLIDAY_UPDATE);

        HolidayResponse response =
                holidayService.updateHoliday(id, request, user);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Holiday updated", response)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHoliday(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        permissionService.check(user, Permission.HOLIDAY_DELETE);

        holidayService.deleteHoliday(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Holiday deleted", null)
        );
    }
}