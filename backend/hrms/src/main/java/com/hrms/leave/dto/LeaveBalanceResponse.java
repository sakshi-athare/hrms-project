package com.hrms.leave.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class LeaveBalanceResponse {

    private String leaveType;

    private BigDecimal totalLeaves;

    private BigDecimal usedLeaves;

    private BigDecimal remainingLeaves;

}