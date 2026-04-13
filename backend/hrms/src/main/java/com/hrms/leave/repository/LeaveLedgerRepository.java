package com.hrms.leave.repository;

import com.hrms.leave.entity.LeaveLedger;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveLedgerRepository extends JpaRepository<LeaveLedger, Long> {

    boolean existsByEmployeeIdAndLeaveTypeIdAndYearAndIsAnnualAllocation(
            Long employeeId,
            Long leaveTypeId,
            Integer year,
            Boolean isAnnualAllocation
    );

    List<LeaveLedger> findByEmployeeIdAndLeaveTypeIdAndYear(
            Long employeeId,
            Long leaveTypeId,
            Integer year
    );
}