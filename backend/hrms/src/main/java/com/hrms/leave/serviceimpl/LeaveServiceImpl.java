package com.hrms.leave.serviceimpl;

import com.hrms.common.enums.LeaveStatus;
import com.hrms.common.enums.LedgerTransactionType;
import com.hrms.common.exception.InvalidLeaveStateException;
import com.hrms.common.exception.ResourceNotFoundException;
import com.hrms.common.exception.UnauthorizedActionException;
import com.hrms.leave.dto.ApplyLeaveRequest;
import com.hrms.leave.dto.LeaveBalanceResponse;
import com.hrms.leave.dto.LeaveResponse;
import com.hrms.leave.entity.LeaveLedger;
import com.hrms.leave.entity.LeavePolicyVersion;
import com.hrms.leave.entity.LeaveRequest;
import com.hrms.leave.entity.LeaveType;
import com.hrms.leave.mapper.LeaveMapper;
import com.hrms.leave.repository.LeaveLedgerRepository;
import com.hrms.leave.repository.LeaveLogRepository;
import com.hrms.leave.repository.LeavePolicyVersionRepository;
import com.hrms.leave.repository.LeaveRequestRepository;
import com.hrms.leave.repository.LeaveTypeRepository;
import com.hrms.leave.service.LeaveService;
import com.hrms.user.entity.User;
import com.hrms.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveServiceImpl implements LeaveService {

	private final LeaveRequestRepository leaveRequestRepository;
	private final LeaveTypeRepository leaveTypeRepository;
	private final LeaveLedgerRepository leaveLedgerRepository;
	private final LeaveLogRepository leaveLogRepository;
	private final UserRepository userRepository;
	private final LeavePolicyVersionRepository leavePolicyVersionRepository;

	/* APPLY LEAVE */

	@Override
	public LeaveResponse applyLeave(Long userId, ApplyLeaveRequest request) {

		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		LeaveType leaveType = leaveTypeRepository.findById(request.getLeaveTypeId())
				.orElseThrow(() -> new ResourceNotFoundException("Leave type not found"));

		/* VALIDATE DATES */

		if (request.getEndDate().isBefore(request.getStartDate())) {
			throw new InvalidLeaveStateException("End date cannot be before start date");
		}

		if (request.getStartDate().isBefore(LocalDate.now())) {
			throw new InvalidLeaveStateException("Cannot apply leave in the past");
		}

		int year = LocalDate.now().getYear();

		/* CHECK OVERLAPPING LEAVE */

		boolean overlapping = leaveRequestRepository
				.existsByEmployeeIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(userId,
						LeaveStatus.PENDING, request.getEndDate(), request.getStartDate());

		if (overlapping) {
			throw new InvalidLeaveStateException("Leave dates overlap with existing leave");
		}

		/* CHECK IF ANNUAL ALLOCATION EXISTS */

		boolean allocationExists = leaveLedgerRepository.existsByEmployeeIdAndLeaveTypeIdAndYearAndIsAnnualAllocation(
				userId, request.getLeaveTypeId(), year, true);

		/* CREATE ANNUAL ALLOCATION IF NOT EXISTS */

		if (!allocationExists) {

			LeavePolicyVersion policyVersion = leavePolicyVersionRepository
					.findTopByPolicyLeaveTypeIdAndActiveTrueOrderByCreatedAtDesc(leaveType.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Active leave policy version not found"));

			BigDecimal annualLimit = BigDecimal.valueOf(policyVersion.getAnnualLimit());

			LeaveLedger allocation = LeaveLedger.builder().employee(user).leaveType(leaveType).amount(annualLimit)
					.transactionType(LedgerTransactionType.CREDIT).year(year).description("Annual leave allocation")
					.isAnnualAllocation(true).build();

			leaveLedgerRepository.save(allocation);
		}

		/* CALCULATE DAYS */

		long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;

		/* CHECK BALANCE */

		List<LeaveLedger> entries = leaveLedgerRepository.findByEmployeeIdAndLeaveTypeIdAndYear(userId,
				leaveType.getId(), year);

		BigDecimal balance = entries.stream().map(LeaveLedger::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

		if (balance.compareTo(BigDecimal.valueOf(days)) < 0) {
			throw new InvalidLeaveStateException("Insufficient leave balance");
		}

		/* CREATE LEAVE REQUEST */

		LeaveRequest leave = LeaveRequest.builder().employee(user).leaveType(leaveType)
				.startDate(request.getStartDate()).endDate(request.getEndDate()).totalDays(BigDecimal.valueOf(days))
				.reason(request.getReason()).status(LeaveStatus.PENDING).build();

		leaveRequestRepository.save(leave);

		return LeaveMapper.toResponse(leave);
	}
	/* APPROVE LEAVE */

	@Override
	public LeaveResponse approveLeave(Long leaveId, Long managerId) {

		LeaveRequest leave = leaveRequestRepository.findById(leaveId)
				.orElseThrow(() -> new ResourceNotFoundException("Leave not found"));

		User manager = userRepository.findById(managerId)
				.orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

		if (leave.getStatus() != LeaveStatus.PENDING) {
			throw new InvalidLeaveStateException("Only pending leaves can be approved");
		}

		leave.setStatus(LeaveStatus.APPROVED);
		leave.setProcessedBy(manager);

		leaveRequestRepository.save(leave);

		/* Deduct leave balance */
		LeaveLedger ledger = LeaveLedger.builder().employee(leave.getEmployee()).leaveType(leave.getLeaveType())
				.amount(leave.getTotalDays().negate()).year(LocalDate.now().getYear())
				.transactionType(LedgerTransactionType.DEBIT).description("Leave approved")
				.referenceLeaveId(leave.getId()).isAnnualAllocation(false).build();

		leaveLedgerRepository.save(ledger);

		return LeaveMapper.toResponse(leave);
	}

	/* REJECT LEAVE */

	@Override
	public LeaveResponse rejectLeave(Long leaveId, Long managerId, String reason) {

		LeaveRequest leave = leaveRequestRepository.findById(leaveId)
				.orElseThrow(() -> new ResourceNotFoundException("Leave not found"));

		User manager = userRepository.findById(managerId)
				.orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

		if (leave.getStatus() != LeaveStatus.PENDING) {
			throw new InvalidLeaveStateException("Only pending leaves can be rejected");
		}

		leave.setStatus(LeaveStatus.REJECTED);
		leave.setProcessedBy(manager);

		leaveRequestRepository.save(leave);

		return LeaveMapper.toResponse(leave);
	}

	/* CANCEL LEAVE */

	@Override
	public LeaveResponse cancelLeave(Long leaveId, Long userId) {

		LeaveRequest leave = leaveRequestRepository.findById(leaveId)
				.orElseThrow(() -> new ResourceNotFoundException("Leave not found"));

		if (!leave.getEmployee().getId().equals(userId)) {
			throw new UnauthorizedActionException("You can cancel only your own leave");
		}

		if (leave.getStatus() == LeaveStatus.CANCELLED) {
			throw new InvalidLeaveStateException("Leave already cancelled");
		}

		/* RESTORE BALANCE IF APPROVED */

		if (leave.getStatus() == LeaveStatus.APPROVED) {

			LeaveLedger ledger = LeaveLedger.builder().employee(leave.getEmployee()).leaveType(leave.getLeaveType())
					.amount(leave.getTotalDays()).year(LocalDate.now().getYear())
					.transactionType(LedgerTransactionType.CREDIT).description("Leave cancelled")
					.referenceLeaveId(leave.getId()).isAnnualAllocation(false).build();

			leaveLedgerRepository.save(ledger);
		}

		leave.setStatus(LeaveStatus.CANCELLED);

		leaveRequestRepository.save(leave);

		return LeaveMapper.toResponse(leave);
	}

	/* MY LEAVES */

	@Override
	public List<LeaveResponse> getMyLeaves(Long userId) {

		return leaveRequestRepository.findByEmployeeId(userId).stream().map(LeaveMapper::toResponse).toList();
	}

	/* PENDING LEAVES */

	@Override
	public List<LeaveResponse> getPendingLeaves(Long managerId) {

		return leaveRequestRepository.findByEmployeeManagerIdAndStatus(managerId, LeaveStatus.PENDING).stream()
				.map(LeaveMapper::toResponse).toList();
	}

	/* LEAVE BALANCE */

	@Override
	public LeaveBalanceResponse getLeaveBalance(Long userId, Long leaveTypeId) {

		int year = LocalDate.now().getYear();

		List<LeaveLedger> entries = leaveLedgerRepository.findByEmployeeIdAndLeaveTypeIdAndYear(userId, leaveTypeId,
				year);

		BigDecimal total = entries.stream().map(LeaveLedger::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal used = entries.stream().filter(e -> e.getAmount().compareTo(BigDecimal.ZERO) < 0)
				.map(LeaveLedger::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).abs();

		BigDecimal remaining = total;

		return LeaveBalanceResponse.builder().leaveType("Leave").totalLeaves(total).usedLeaves(used)
				.remainingLeaves(remaining).build();
	}
}