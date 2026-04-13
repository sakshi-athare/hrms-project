package com.hrms.leave.repository;

import com.hrms.leave.entity.LeavePolicy;
import org.springframework.data.jpa.repository.JpaRepository;


import com.hrms.common.enums.Role;


import java.util.List;
import java.util.Optional;

public interface LeavePolicyRepository extends JpaRepository<LeavePolicy, Long> {

    List<LeavePolicy> findByRole(Role role);

    Optional<LeavePolicy> findByRoleAndLeaveTypeId(Role role, Long leaveTypeId);

}