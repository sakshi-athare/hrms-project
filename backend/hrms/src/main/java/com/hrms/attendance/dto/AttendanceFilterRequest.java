package com.hrms.attendance.dto;


import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendanceFilterRequest {

    private String search; // name/email

    private LocalDate startDate;
    private LocalDate endDate;

    private String status; // PRESENT / ABSENT / HALF_DAY

    private int page = 0;
    private int size = 10;
}