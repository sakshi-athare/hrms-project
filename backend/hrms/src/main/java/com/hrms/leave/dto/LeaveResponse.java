package com.hrms.leave.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.hrms.common.enums.LeaveStatus;

@Getter
@Builder
public class LeaveResponse {

    private Long id;

    private Long employeeId;

    private String employeeName;

    private String leaveType;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal totalDays;

    private LeaveStatus status;

    private String reason;

}