package com.hrms.attendance.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hrms.common.enums.AttendanceStatus;

import lombok.Data;

@Data
public class TodayAttendanceResponse {

    private LocalDate date;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    private AttendanceStatus status;

    private boolean isLate;

    private BigDecimal workingHours;

}