package com.hrms.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hrms.attendance.entity.AttendanceLog;

public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {
}