package com.hrms.leave.mapper;

import com.hrms.leave.dto.LeavePolicyResponse;
import com.hrms.leave.entity.LeavePolicy;

public class LeavePolicyMapper {

    private LeavePolicyMapper() {}

    public static LeavePolicyResponse toResponse(LeavePolicy policy) {

        return LeavePolicyResponse.builder()
                .id(policy.getId())
                .role(policy.getRole())
                .leaveType(policy.getLeaveType().getName())
                .build();
    }
}