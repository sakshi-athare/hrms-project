package com.hrms.leave.dto;

import com.hrms.common.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateLeavePolicyRequest {

    private Role role;

    private Long leaveTypeId;

}