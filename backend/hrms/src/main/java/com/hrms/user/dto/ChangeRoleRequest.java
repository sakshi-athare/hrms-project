package com.hrms.user.dto;

import com.hrms.common.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeRoleRequest {

    @NotNull
    private Role role;
}