package com.hrms.attendance.dto;

import com.hrms.attendance.entity.Attendance;

public class AttendanceMapper {
	
	public static AttendanceResponse toDto(Attendance a) {

	    return AttendanceResponse.builder()
	            .id(a.getId())
	            .userId(a.getUser().getId())
	            .employeeName(a.getUser().getUsername())
	            .email(a.getUser().getEmail())
	            .date(a.getDate())
	            .checkInTime(a.getCheckInTime())
	            .checkOutTime(a.getCheckOutTime())
	            .totalWorkHours(a.getTotalWorkHours())
	            .late(a.isLate())
	            .status(a.getStatus() != null ? a.getStatus().name() : null)
	            .build();
	}

}
