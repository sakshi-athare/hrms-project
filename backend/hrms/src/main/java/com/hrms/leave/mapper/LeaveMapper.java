package com.hrms.leave.mapper;

import com.hrms.leave.dto.LeaveResponse;
import com.hrms.leave.entity.LeaveRequest;

public class LeaveMapper {

    private LeaveMapper() {
    }

    public static LeaveResponse toResponse(LeaveRequest leave) {

        return LeaveResponse.builder()
                .id(leave.getId())
                .employeeId(leave.getEmployee().getId())
                .employeeName(leave.getEmployee().getUsername())
                .leaveType(leave.getLeaveType().getName())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .totalDays(leave.getTotalDays())
                .status(leave.getStatus())
                .reason(leave.getReason())
                .build();
    }
}