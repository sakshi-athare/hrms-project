package com.hrms.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hrms.attendance.entity.Attendance;
import com.hrms.attendance.entity.AttendanceRegularization;
import com.hrms.attendance.enums.RegularizationStatus;

public interface AttendanceRegularizationRepository
        extends JpaRepository<AttendanceRegularization, Long> {


	boolean existsByAttendanceAndStatus(Attendance attendance, RegularizationStatus status);
	List<AttendanceRegularization> findByStatus(RegularizationStatus status);	
	
}