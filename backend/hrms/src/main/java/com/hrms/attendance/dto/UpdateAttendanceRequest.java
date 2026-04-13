package com.hrms.attendance.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UpdateAttendanceRequest {

    private LocalDateTime checkIn;

    private LocalDateTime checkOut;

    private String note;

}