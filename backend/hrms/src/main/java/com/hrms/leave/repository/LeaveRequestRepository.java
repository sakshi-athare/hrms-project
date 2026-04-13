package com.hrms.leave.repository;

import com.hrms.common.enums.LeaveStatus;
import com.hrms.leave.entity.LeaveRequest;
import com.hrms.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployeeId(Long employeeId);

    List<LeaveRequest> findByStatus(LeaveStatus status);

    List<LeaveRequest> findByEmployeeManagerIdAndStatus(Long managerId, LeaveStatus status);

    List<LeaveRequest> findByEmployeeIdAndStartDateBetween(Long employeeId, LocalDate start, LocalDate end);
    boolean existsByEmployeeIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long employeeId,
            LeaveStatus status,
            LocalDate endDate,
            LocalDate startDate
    );
    
    boolean existsByEmployeeAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            User employee,
            LeaveStatus status,
            LocalDate startDate,
            LocalDate endDate
    );
    
    long countByStatus(LeaveStatus status);
    
    long countByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            LeaveStatus status,
            LocalDate currentDate1,
            LocalDate currentDate2
    );

    long countByEmployee_IdAndStatus(Long employeeId, LeaveStatus status);
}