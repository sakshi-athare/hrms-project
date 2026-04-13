package com.hrms.attendance.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ManualAttendanceRequest {

    private Long employeeId;

    private LocalDate date;

    private LocalDateTime checkIn;

    private LocalDateTime checkOut;
}