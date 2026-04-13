package com.hrms.leave.repository;

import com.hrms.leave.entity.LeaveLog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveLogRepository extends JpaRepository<LeaveLog, Long> {

    List<LeaveLog> findByLeaveIdOrderByCreatedAtAsc(Long leaveId);

}