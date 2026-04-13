package com.hrms.department.dto;

import lombok.Data;

@Data
public class CreateDepartmentRequest {

    private String name;
    private Long managerId;

 
}