package com.hrms.department.serviceimpl;

import com.hrms.common.exception.BadRequestException;
import com.hrms.common.exception.ResourceNotFoundException;
import com.hrms.department.dto.CreateDepartmentRequest;
import com.hrms.department.dto.UpdateDepartmentRequest;
import com.hrms.department.entity.Department;
import com.hrms.department.repository.DepartmentRepository;
import com.hrms.department.service.DepartmentService;
import com.hrms.user.entity.User;
import com.hrms.user.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository,
                                 UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createDepartment(CreateDepartmentRequest request) {

        if (departmentRepository.existsByName(request.getName())) {
            throw new BadRequestException("Department already exists");
        }

        Department department = new Department();

        department.setName(request.getName());

        if (request.getManagerId() != null) {

            User manager = userRepository.findById(request.getManagerId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Manager not found"));

            department.setManager(manager);
        }

        departmentRepository.save(department);
    }

    @Override
    public void updateDepartment(Long id, UpdateDepartmentRequest request) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));

        if (request.getName() != null) {
            department.setName(request.getName());
        }

        if (request.getManagerId() != null) {

            User manager = userRepository.findById(request.getManagerId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Manager not found"));

            department.setManager(manager);
        }

        departmentRepository.save(department);
    }

    @Override
    public void deleteDepartment(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));

        departmentRepository.delete(department);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
}