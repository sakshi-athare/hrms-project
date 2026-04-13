package com.hrms.leave.serviceimpl;


import com.hrms.leave.dto.CreateLeaveTypeRequest;
import com.hrms.leave.dto.LeaveTypeResponse;
import com.hrms.leave.entity.LeaveType;
import com.hrms.leave.mapper.LeaveTypeMapper;
import com.hrms.leave.repository.LeaveTypeRepository;
import com.hrms.leave.service.LeaveTypeService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveTypeServiceImpl implements LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;

    @Override
    public LeaveTypeResponse createLeaveType(CreateLeaveTypeRequest request) {

        if (leaveTypeRepository.existsByName(request.getName())) {
            throw new RuntimeException("Leave type already exists");
        }

        LeaveType leaveType = LeaveType.builder()
                .name(request.getName())
                .paid(request.isPaid())
                .active(true)
                .build();
        
        leaveTypeRepository.save(leaveType);

        return LeaveTypeMapper.toResponse(leaveType);
    }

    @Override
    public List<LeaveTypeResponse> getLeaveTypes() {

        return leaveTypeRepository.findAll()
                .stream()
                .map(LeaveTypeMapper::toResponse)
                .toList();
    }
}