package com.hrms.attendance.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class RegularizationRequestDto {

    private Long attendanceId;

    private LocalTime checkIn;

    private LocalTime checkOut;

    private String reason;

}