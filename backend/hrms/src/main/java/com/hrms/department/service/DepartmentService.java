package com.hrms.department.service;

import com.hrms.department.dto.CreateDepartmentRequest;
import com.hrms.department.dto.UpdateDepartmentRequest;

import java.util.List;

public interface DepartmentService {

    void createDepartment(CreateDepartmentRequest request);

    void updateDepartment(Long id, UpdateDepartmentRequest request);

    void deleteDepartment(Long id);

    List<?> getAllDepartments();
}