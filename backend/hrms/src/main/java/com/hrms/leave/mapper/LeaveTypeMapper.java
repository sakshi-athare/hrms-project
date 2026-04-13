package com.hrms.leave.mapper;

import com.hrms.leave.dto.LeaveTypeResponse;
import com.hrms.leave.entity.LeaveType;

public class LeaveTypeMapper {

    private LeaveTypeMapper() {}

    public static LeaveTypeResponse toResponse(LeaveType type) {

        return LeaveTypeResponse.builder()
                .id(type.getId())
                .name(type.getName())
                .paid(type.getPaid())
                .active(type.getActive())
                .build();
    }

}