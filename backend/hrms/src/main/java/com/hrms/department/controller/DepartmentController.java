package com.hrms.department.controller;

import com.hrms.common.response.ApiResponse;
import com.hrms.department.dto.CreateDepartmentRequest;
import com.hrms.department.dto.UpdateDepartmentRequest;
import com.hrms.department.service.DepartmentService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ApiResponse<?> createDepartment(
            @RequestBody CreateDepartmentRequest request) {

        departmentService.createDepartment(request);

        return new ApiResponse<>(true,
                "Department created successfully",
                null);
    }

    @GetMapping
    public ApiResponse<?> getDepartments() {

        return new ApiResponse<>(true,
                "Departments fetched",
                departmentService.getAllDepartments());
    }

    @PatchMapping("/{id}")
    public ApiResponse<?> updateDepartment(
            @PathVariable Long id,
            @RequestBody UpdateDepartmentRequest request) {

        departmentService.updateDepartment(id, request);

        return new ApiResponse<>(true,
                "Department updated",
                null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteDepartment(@PathVariable Long id) {

        departmentService.deleteDepartment(id);

        return new ApiResponse<>(true,
                "Department deleted",
                null);
    }
}