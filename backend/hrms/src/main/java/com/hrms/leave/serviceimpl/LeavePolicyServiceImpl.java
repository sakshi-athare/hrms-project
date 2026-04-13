package com.hrms.leave.serviceimpl;

import com.hrms.leave.dto.CreateLeavePolicyRequest;
import com.hrms.leave.dto.LeavePolicyResponse;
import com.hrms.leave.entity.LeavePolicy;
import com.hrms.leave.entity.LeaveType;
import com.hrms.leave.mapper.LeavePolicyMapper;
import com.hrms.leave.repository.LeavePolicyRepository;
import com.hrms.leave.repository.LeaveTypeRepository;
import com.hrms.leave.service.LeavePolicyService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeavePolicyServiceImpl implements LeavePolicyService {

    private final LeavePolicyRepository leavePolicyRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    @Override
    public LeavePolicyResponse createPolicy(CreateLeavePolicyRequest request) {

        LeaveType leaveType = leaveTypeRepository.findById(request.getLeaveTypeId())
                .orElseThrow(() -> new RuntimeException("Leave type not found"));

        LeavePolicy policy = LeavePolicy.builder()
                .role(request.getRole())
                .leaveType(leaveType)
                .build();

        leavePolicyRepository.save(policy);

        return LeavePolicyMapper.toResponse(policy);
    }

    @Override
    public List<LeavePolicyResponse> getPolicies() {

        return leavePolicyRepository.findAll()
                .stream()
                .map(LeavePolicyMapper::toResponse)
                .toList();
    }
}