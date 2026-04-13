package com.hrms.user.dto;

import lombok.Data;

@Data
public class UpdateUserStatusRequest {
    private Boolean isActive;
}