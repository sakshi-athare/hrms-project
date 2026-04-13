package com.hrms.attendance.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegularizationResponse {

    private Long id;

    private Long attendanceId;

    private String employeeName;
    private String email;

    private LocalDateTime requestedCheckIn;
    private LocalDateTime requestedCheckOut;

    private String reason;

    private String status;

    private String reviewedBy;

    private String rejectionReason;
}