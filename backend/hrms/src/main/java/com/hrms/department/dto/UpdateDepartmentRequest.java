package com.hrms.department.dto;

import lombok.Data;

@Data
public class UpdateDepartmentRequest {
	
	private String name;
    private Long managerId;

}
