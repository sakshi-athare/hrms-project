package com.hrms.leave.mapper;

import com.hrms.leave.dto.LeavePolicyVersionResponse;
import com.hrms.leave.entity.LeavePolicyVersion;

public class LeavePolicyVersionMapper {

    private LeavePolicyVersionMapper() {}

    public static LeavePolicyVersionResponse toResponse(LeavePolicyVersion version) {

        return LeavePolicyVersionResponse.builder()
                .id(version.getId())
                .policyId(version.getPolicy().getId())
                .versionNumber(version.getVersionNumber())
                .annualLimit(version.getAnnualLimit())
                .carryForwardLimit(version.getCarryForwardLimit())
                .effectiveFrom(version.getEffectiveFrom())
                .effectiveTo(version.getEffectiveTo())
                .build();
    }
}