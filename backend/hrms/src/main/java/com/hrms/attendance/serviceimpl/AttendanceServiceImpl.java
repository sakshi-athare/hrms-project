package com.hrms.attendance.serviceimpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.hrms.attendance.dto.AttendanceCalendarResponse;
import com.hrms.attendance.dto.AttendanceFilterRequest;
import com.hrms.attendance.dto.AttendanceMapper;
import com.hrms.attendance.dto.AttendanceResponse;
import com.hrms.attendance.dto.ManualAttendanceRequest;
import com.hrms.attendance.dto.RegularizationMapper;
import com.hrms.attendance.dto.RegularizationRequestDto;
import com.hrms.attendance.dto.RegularizationResponse;
import com.hrms.attendance.dto.TodayAttendanceResponse;
import com.hrms.attendance.dto.UpdateAttendanceRequest;
import com.hrms.attendance.entity.Attendance;
import com.hrms.attendance.entity.AttendanceLog;
import com.hrms.attendance.entity.AttendanceRegularization;
import com.hrms.attendance.enums.RegularizationStatus;
import com.hrms.attendance.repository.AttendanceLogRepository;
import com.hrms.attendance.repository.AttendanceRegularizationRepository;
import com.hrms.attendance.repository.AttendanceRepository;
import com.hrms.attendance.service.AttendanceService;
import com.hrms.common.enums.AttendanceStatus;
import com.hrms.common.enums.LeaveStatus;
import com.hrms.common.exception.BadRequestException;
import com.hrms.common.exception.ResourceNotFoundException;
import com.hrms.common.exception.UnauthorizedActionException;
import com.hrms.holiday.entity.Holiday;
import com.hrms.holiday.repository.HolidayRepository;
import com.hrms.leave.entity.LeaveRequest;
import com.hrms.leave.repository.LeaveRequestRepository;
import com.hrms.organization.entity.OrganizationSettings;
import com.hrms.organization.repository.OrganizationSettingsRepository;
import com.hrms.user.entity.User;
import com.hrms.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

	private final AttendanceRepository attendanceRepository;
	private final AttendanceLogRepository logRepository;
	private final HolidayRepository holidayRepository;
	private final LeaveRequestRepository leaveRepository;
	private final OrganizationSettingsRepository settingsRepository;
	private final UserRepository userRepository;
	private final AttendanceRegularizationRepository regularizationRepository;

	private boolean isHoliday(LocalDate date) {
		return holidayRepository.existsByDate(date);
	}

	private void applyAttendanceRules(Attendance attendance, OrganizationSettings settings) {

		// 🔹 Late calculation
		if (attendance.getCheckInTime() != null) {

			LocalDateTime officeStart = LocalDateTime.of(attendance.getDate(), settings.getOfficeStartTime());

			LocalDateTime grace = officeStart.plusMinutes(settings.getGraceMinutes());

			attendance.setLate(attendance.getCheckInTime().isAfter(grace));
		}

		// 🔹 Work hours + status
		if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {

			Duration duration = Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime());

			BigDecimal hours = BigDecimal.valueOf(duration.toMinutes()).divide(BigDecimal.valueOf(60), 2,
					RoundingMode.HALF_UP);

			attendance.setTotalWorkHours(hours);

			// 🔥 status logic from settings
			if (hours.compareTo(settings.getFullDayHours()) < 0) {
				attendance.setStatus(AttendanceStatus.HALF_DAY);
			} else {
				attendance.setStatus(AttendanceStatus.PRESENT);
			}
		}
	}

	private boolean hasApprovedLeave(User user, LocalDate date) {

		return leaveRepository.existsByEmployeeAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(user,
				LeaveStatus.APPROVED, date, date);
	}

	private OrganizationSettings getOrgSettings() {

		return settingsRepository.findFirstByOrderByIdAsc()
				.orElseThrow(() -> new BadRequestException("Organization settings not configured"));
	}

	private User getCurrentUser() {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails userDetails) {

			String email = userDetails.getUsername();

			return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		}

		throw new RuntimeException("Invalid authentication");
	}

	@Override
	public AttendanceResponse checkIn(String email) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		LocalDate today = LocalDate.now();

		if (isHoliday(today)) {
			throw new BadRequestException("Today is a holiday");
		}

		if (hasApprovedLeave(user, today)) {
			throw new BadRequestException("You have approved leave today");
		}

		Attendance attendance = attendanceRepository.findByUserAndDate(user, today)
				.orElseGet(() -> Attendance.builder().user(user).date(today).build());

		if (attendance.getCheckInTime() != null) {
			throw new BadRequestException("Already checked in");
		}

		OrganizationSettings settings = getOrgSettings();

		LocalDateTime now = LocalDateTime.now();
		attendance.setCheckInTime(now);

		LocalDateTime officeStart = LocalDateTime.of(today, settings.getOfficeStartTime());
		LocalDateTime grace = officeStart.plusMinutes(settings.getGraceMinutes());

		attendance.setLate(now.isAfter(grace));
		attendance.setStatus(AttendanceStatus.IN_PROGRESS);

		attendanceRepository.save(attendance);

		logRepository.save(AttendanceLog.builder().attendance(attendance).action("CHECK_IN").performedBy(user).build());

		return AttendanceMapper.toDto(attendance);
	}

	@Override
	public AttendanceResponse checkOut(String email) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		LocalDate today = LocalDate.now();

		Attendance attendance = attendanceRepository.findByUserAndDate(user, today)
				.orElseThrow(() -> new BadRequestException("No check-in found"));

		if (attendance.getCheckOutTime() != null) {
			throw new BadRequestException("Already checked out");
		}

		LocalDateTime now = LocalDateTime.now();
		attendance.setCheckOutTime(now);

		Duration duration = Duration.between(attendance.getCheckInTime(), now);

		BigDecimal hours = BigDecimal.valueOf(duration.toMinutes()).divide(BigDecimal.valueOf(60), 2,
				RoundingMode.HALF_UP);

		attendance.setTotalWorkHours(hours);

		OrganizationSettings settings = getOrgSettings();

		if (hours.compareTo(settings.getFullDayHours()) < 0) {
			attendance.setStatus(AttendanceStatus.HALF_DAY);
		} else {
			attendance.setStatus(AttendanceStatus.PRESENT);
		}
		attendanceRepository.save(attendance);

		logRepository
				.save(AttendanceLog.builder().attendance(attendance).action("CHECK_OUT").performedBy(user).build());

		return AttendanceMapper.toDto(attendance);
	}

	@Override
	public TodayAttendanceResponse getTodayAttendance(String email) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		LocalDate today = LocalDate.now();

		Attendance attendance = attendanceRepository.findByUserAndDate(user, today).orElse(null);

		TodayAttendanceResponse res = new TodayAttendanceResponse();
		res.setDate(today);

		if (attendance == null) {
			res.setWorkingHours(BigDecimal.ZERO);
			return res;
		}

		res.setCheckInTime(attendance.getCheckInTime());
		res.setCheckOutTime(attendance.getCheckOutTime());
		res.setStatus(attendance.getStatus());
		res.setLate(attendance.isLate());
		res.setWorkingHours(attendance.getTotalWorkHours());

		return res;
	}

	@Override
	public List<AttendanceResponse> getAttendanceHistory(User user) {

		return attendanceRepository.findByUserOrderByDateDesc(user).stream().map(AttendanceMapper::toDto).toList();

	}

	@Override
	public AttendanceResponse createManualAttendance(ManualAttendanceRequest request, String email) {

		User hr = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		User employee = userRepository.findById(request.getEmployeeId())
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

		if (hasApprovedLeave(employee, request.getDate())) {
			throw new BadRequestException("Employee has approved leave");
		}

		if (isHoliday(request.getDate())) {
			throw new BadRequestException("Holiday");
		}

		if (attendanceRepository.findByUserAndDate(employee, request.getDate()).isPresent()) {
			throw new BadRequestException("Already exists");
		}

		OrganizationSettings settings = getOrgSettings();

		Duration duration = Duration.between(request.getCheckIn(), request.getCheckOut());

		BigDecimal hours = BigDecimal.valueOf(duration.toMinutes()).divide(BigDecimal.valueOf(60), 2,
				RoundingMode.HALF_UP);

		Attendance attendance = new Attendance();

		attendance.setUser(employee);
		attendance.setDate(request.getDate());
		attendance.setCheckInTime(request.getCheckIn());
		attendance.setCheckOutTime(request.getCheckOut());
		attendance.setTotalWorkHours(hours);

		attendance.setStatus(
				hours.compareTo(settings.getFullDayHours()) < 0 ? AttendanceStatus.HALF_DAY
						: AttendanceStatus.PRESENT);

		attendanceRepository.save(attendance);

		logRepository
				.save(AttendanceLog.builder().attendance(attendance).action("HR_MANUAL_ENTRY").performedBy(hr).build());

		return AttendanceMapper.toDto(attendance);
	}

	@Override
	public AttendanceResponse updateAttendance(Long id, UpdateAttendanceRequest request, String email) {

		User hr = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		Attendance attendance = attendanceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));

		LocalDateTime oldIn = attendance.getCheckInTime();
		LocalDateTime oldOut = attendance.getCheckOutTime();

		if (request.getCheckIn() != null) {
			attendance.setCheckInTime(request.getCheckIn());
		}

		if (request.getCheckOut() != null) {
			attendance.setCheckOutTime(request.getCheckOut());
		}

		OrganizationSettings settings = getOrgSettings();

		// late recalculation
		if (attendance.getCheckInTime() != null) {
			LocalDateTime officeStart = LocalDateTime.of(attendance.getDate(), settings.getOfficeStartTime());

			LocalDateTime grace = officeStart.plusMinutes(settings.getGraceMinutes());
			attendance.setLate(attendance.getCheckInTime().isAfter(grace));
		}

		// hours recalculation
		if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {

			Duration duration = Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime());

			BigDecimal hours = BigDecimal.valueOf(duration.toMinutes()).divide(BigDecimal.valueOf(60), 2,
					RoundingMode.HALF_UP);

			attendance.setTotalWorkHours(hours);

			attendance.setStatus(
					hours.compareTo(settings.getFullDayHours()) < 0 ? AttendanceStatus.HALF_DAY
							: AttendanceStatus.PRESENT);
		}

		attendanceRepository.save(attendance);

		logRepository.save(AttendanceLog.builder().attendance(attendance).action("HR_EDIT").performedBy(hr)
				.note("Updated attendance").build());

		return AttendanceMapper.toDto(attendance);
	}

	@Override
	public void requestRegularization(RegularizationRequestDto request, String email) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		Attendance attendance = attendanceRepository.findById(request.getAttendanceId())
				.orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));

		// ownership check (THIS stays in service)
		if (!attendance.getUser().getId().equals(user.getId())) {
			throw new UnauthorizedActionException("Cannot regularize others attendance");
		}

		if (regularizationRepository.existsByAttendanceAndStatus(attendance, RegularizationStatus.PENDING)) {
			throw new BadRequestException("Already pending");
		}

		LocalDate date = attendance.getDate();

		LocalDateTime checkIn = request.getCheckIn() != null ? LocalDateTime.of(date, request.getCheckIn()) : null;

		LocalDateTime checkOut = request.getCheckOut() != null ? LocalDateTime.of(date, request.getCheckOut()) : null;

		AttendanceRegularization reg = AttendanceRegularization.builder().attendance(attendance)
				.requestedCheckIn(checkIn).requestedCheckOut(checkOut).reason(request.getReason())
				.status(RegularizationStatus.PENDING).build();

		regularizationRepository.save(reg);
	}

	@Override
	@Transactional
	public void approveRegularization(Long id, String email) {

		User hr = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		AttendanceRegularization reg = regularizationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not found"));

		if (reg.getStatus() != RegularizationStatus.PENDING) {
			throw new BadRequestException("Already processed");
		}

		Attendance attendance = reg.getAttendance();
		OrganizationSettings settings = getOrgSettings();

		if (reg.getRequestedCheckIn() != null) {
			attendance.setCheckInTime(reg.getRequestedCheckIn());
		}

		if (reg.getRequestedCheckOut() != null) {
			attendance.setCheckOutTime(reg.getRequestedCheckOut());
		}

		// recalc late
		if (attendance.getCheckInTime() != null) {
			LocalDateTime officeStart = LocalDateTime.of(attendance.getDate(), settings.getOfficeStartTime());

			LocalDateTime grace = officeStart.plusMinutes(settings.getGraceMinutes());
			attendance.setLate(attendance.getCheckInTime().isAfter(grace));
		}

		// recalc hours
		if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {

			Duration duration = Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime());

			BigDecimal hours = BigDecimal.valueOf(duration.toMinutes()).divide(BigDecimal.valueOf(60), 2,
					RoundingMode.HALF_UP);

			attendance.setTotalWorkHours(hours);

			attendance.setStatus(
					hours.compareTo(settings.getFullDayHours()) < 0 ? AttendanceStatus.HALF_DAY
							: AttendanceStatus.PRESENT);
		}

		attendanceRepository.save(attendance);

		reg.setStatus(RegularizationStatus.APPROVED);
		reg.setReviewedBy(hr);

		regularizationRepository.save(reg);
	}

	@Override
	@Transactional
	public void rejectRegularization(Long id, String reason, String email) {

		User hr = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		AttendanceRegularization reg = regularizationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not found"));

		if (reg.getStatus() != RegularizationStatus.PENDING) {
			throw new BadRequestException("Already processed");
		}

		reg.setStatus(RegularizationStatus.REJECTED);
		reg.setReviewedBy(hr);
		reg.setRejectionReason(reason);

		regularizationRepository.save(reg);
	}

	@Override
	public List<RegularizationResponse> getPendingRegularizations() {

		return regularizationRepository.findByStatus(RegularizationStatus.PENDING).stream()
				.map(RegularizationMapper::toDto).toList();
	}

	@Override
	public List<AttendanceCalendarResponse> getAttendanceCalendar(String email, int year, int month) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		LocalDate start = LocalDate.of(year, month, 1);
		LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

		List<Attendance> attendanceList = attendanceRepository.findByUserAndDateBetween(user, start, end);

		List<LeaveRequest> leaves = leaveRepository.findByEmployeeIdAndStartDateBetween(user.getId(), start, end);

		List<Holiday> holidays = holidayRepository.findByDateBetween(start, end);

		Map<LocalDate, String> map = new HashMap<>();

		attendanceList.forEach(a -> map.put(a.getDate(), a.getStatus().name()));

		leaves.forEach(l -> {
			LocalDate d = l.getStartDate();
			while (!d.isAfter(l.getEndDate())) {
				map.put(d, "LEAVE");
				d = d.plusDays(1);
			}
		});

		holidays.forEach(h -> map.put(h.getDate(), "HOLIDAY"));

		List<AttendanceCalendarResponse> response = new ArrayList<>();

		LocalDate d = start;
		while (!d.isAfter(end)) {
			response.add(new AttendanceCalendarResponse(d, map.getOrDefault(d, "ABSENT")));
			d = d.plusDays(1);
		}

		return response;
	}

	@Override
	public List<AttendanceResponse> getAllAttendance() {

		List<Attendance> list = attendanceRepository.findAllByOrderByDateDesc();

		return list.stream().map(AttendanceMapper::toDto).toList();
	}

	@Override
	public Page<AttendanceResponse> getFilteredAttendance(AttendanceFilterRequest request) {

		AttendanceStatus status = null;

		if (request.getStatus() != null) {
			status = AttendanceStatus.valueOf(request.getStatus());
		}

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

		Page<Attendance> page = attendanceRepository.findFilteredAttendance(request.getSearch(), status,
				request.getStartDate(), request.getEndDate(), pageable);

		return page.map(AttendanceMapper::toDto);
	}

}