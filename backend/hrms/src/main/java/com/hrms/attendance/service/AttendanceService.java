package com.hrms.attendance.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.hrms.attendance.dto.AttendanceCalendarResponse;
import com.hrms.attendance.dto.AttendanceFilterRequest;
import com.hrms.attendance.dto.AttendanceResponse;
import com.hrms.attendance.dto.ManualAttendanceRequest;
import com.hrms.attendance.dto.RegularizationRequestDto;
import com.hrms.attendance.dto.RegularizationResponse;
import com.hrms.attendance.dto.TodayAttendanceResponse;
import com.hrms.attendance.dto.UpdateAttendanceRequest;
import com.hrms.attendance.entity.AttendanceRegularization;
import com.hrms.user.entity.User;

public interface AttendanceService {

	AttendanceResponse checkIn(String email);

	AttendanceResponse checkOut(String email);

	TodayAttendanceResponse getTodayAttendance(String email);

	List<AttendanceResponse> getAttendanceHistory(User user);

	AttendanceResponse createManualAttendance(ManualAttendanceRequest request, String email);

	AttendanceResponse updateAttendance(Long id, UpdateAttendanceRequest request, String email);

	void requestRegularization(RegularizationRequestDto request, String email);

	void approveRegularization(Long id, String email);

	void rejectRegularization(Long id, String reason, String email);

	List<RegularizationResponse> getPendingRegularizations();

	List<AttendanceCalendarResponse> getAttendanceCalendar(String email, int year, int month);
	
	List<AttendanceResponse> getAllAttendance();
	
	Page<AttendanceResponse> getFilteredAttendance(AttendanceFilterRequest request);
}