package com.hrms.leave.dto;

import com.hrms.common.enums.Role;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LeavePolicyResponse {

    private Long id;

    private Role role;

    private String leaveType;

}