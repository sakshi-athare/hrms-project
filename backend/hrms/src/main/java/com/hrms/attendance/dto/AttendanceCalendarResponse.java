package com.hrms.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AttendanceCalendarResponse {

    private LocalDate date;

    private String type;

}