package com.hrms.leave.repository;


import com.hrms.leave.entity.LeavePolicyVersion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LeavePolicyVersionRepository
        extends JpaRepository<LeavePolicyVersion, Long> {

    List<LeavePolicyVersion> findByPolicyId(Long policyId);

    Optional<LeavePolicyVersion> findTopByPolicyIdOrderByVersionNumberDesc(Long policyId);
    Optional<LeavePolicyVersion> findTopByPolicyLeaveTypeIdAndActiveTrueOrderByCreatedAtDesc(
            Long leaveTypeId
    );

    List<LeavePolicyVersion> findByActiveTrue();
}