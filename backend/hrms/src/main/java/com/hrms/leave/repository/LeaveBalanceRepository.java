package com.hrms.leave.repository;

import com.hrms.leave.entity.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {

    Optional<LeaveBalance> findByUserIdAndLeaveTypeId(Long userId, Long leaveTypeId);

}