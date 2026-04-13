package com.hrms.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardSummaryResponse {

    private long totalEmployees;
    private long activeEmployees;
    private long inactiveEmployees;
    private long newJoineesThisMonth;
    private long employeesOnLeave;
    private long pendingLeaveRequests;
}