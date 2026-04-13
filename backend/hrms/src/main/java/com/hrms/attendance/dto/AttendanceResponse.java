package com.hrms.attendance.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceResponse {

    private Long id;

    private Long userId;
    private String employeeName;
    private String email;

    private LocalDate date;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    private BigDecimal totalWorkHours;

    private boolean late;

    private String status;
}