package com.hrms.leave.serviceimpl;

import com.hrms.leave.dto.CreateLeavePolicyVersionRequest;
import com.hrms.leave.dto.LeavePolicyVersionResponse;
import com.hrms.leave.entity.LeavePolicy;
import com.hrms.leave.entity.LeavePolicyVersion;
import com.hrms.leave.mapper.LeavePolicyVersionMapper;
import com.hrms.leave.repository.LeavePolicyRepository;
import com.hrms.leave.repository.LeavePolicyVersionRepository;
import com.hrms.leave.service.LeavePolicyVersionService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeavePolicyVersionServiceImpl implements LeavePolicyVersionService {

    private final LeavePolicyRepository leavePolicyRepository;
    private final LeavePolicyVersionRepository versionRepository;

    @Override
    public LeavePolicyVersionResponse createVersion(CreateLeavePolicyVersionRequest request) {

        LeavePolicy policy = leavePolicyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        LeavePolicyVersion version = new LeavePolicyVersion();

        version.setPolicy(policy);
        version.setAnnualLimit(request.getAnnualLimit());
        version.setCarryForwardLimit(request.getCarryForwardLimit());
        version.setEffectiveFrom(request.getEffectiveFrom());
        version.setEffectiveTo(request.getEffectiveTo());
        version.setActive(true);
        version.setVersionNumber(1);

        versionRepository.save(version);

        return LeavePolicyVersionMapper.toResponse(version);
    }
}