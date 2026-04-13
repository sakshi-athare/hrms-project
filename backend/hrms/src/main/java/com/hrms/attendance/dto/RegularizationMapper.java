package com.hrms.attendance.dto;


import com.hrms.attendance.entity.AttendanceRegularization;

public class RegularizationMapper {

    public static RegularizationResponse toDto(AttendanceRegularization reg) {

        return RegularizationResponse.builder()
                .id(reg.getId())
                .attendanceId(reg.getAttendance().getId())
                .employeeName(reg.getAttendance().getUser().getUsername())
                .email(reg.getAttendance().getUser().getEmail())
                .requestedCheckIn(reg.getRequestedCheckIn())
                .requestedCheckOut(reg.getRequestedCheckOut())
                .reason(reg.getReason())
                .status(reg.getStatus().name())
                .reviewedBy(reg.getReviewedBy() != null
                        ? reg.getReviewedBy().getUsername()
                        : null)
                .rejectionReason(reg.getRejectionReason())
                .build();
    }
}
