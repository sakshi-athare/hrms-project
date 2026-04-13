package com.hrms.leave.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateLeaveTypeRequest {

    @NotBlank
    private String name;
    private boolean paid;

}