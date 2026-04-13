package com.hrms.leave.serviceimpl;

import com.hrms.common.enums.LedgerTransactionType;
import com.hrms.common.exception.ResourceNotFoundException;
import com.hrms.leave.entity.LeaveLedger;
import com.hrms.leave.entity.LeavePolicyVersion;
import com.hrms.leave.entity.LeaveType;
import com.hrms.leave.repository.LeaveLedgerRepository;
import com.hrms.leave.repository.LeavePolicyVersionRepository;
import com.hrms.leave.service.LeaveBalanceService;
import com.hrms.user.entity.User;
import com.hrms.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

	private final UserRepository userRepository;
	private final LeavePolicyVersionRepository leavePolicyVersionRepository;
	private final LeaveLedgerRepository leaveLedgerRepository;

	@Override
	public void generateAnnualLeaveBalances() {

		int year = LocalDate.now().getYear();

		List<User> users = userRepository.findAll();

		List<LeavePolicyVersion> policies = leavePolicyVersionRepository.findByActiveTrue();

		for (User user : users) {

			for (LeavePolicyVersion version : policies) {

				LeaveType leaveType = version.getPolicy().getLeaveType();

				boolean exists = leaveLedgerRepository.existsByEmployeeIdAndLeaveTypeIdAndYearAndIsAnnualAllocation(
						user.getId(), leaveType.getId(), year, true);

				if (exists) {
					continue;
				}

				BigDecimal amount = BigDecimal.valueOf(version.getAnnualLimit());

				LeaveLedger ledger = LeaveLedger.builder().employee(user).leaveType(leaveType).amount(amount)
						.transactionType(LedgerTransactionType.CREDIT).year(year).description("Annual leave allocation")
						.isAnnualAllocation(true).build();

				leaveLedgerRepository.save(ledger);
			}
		}
	}
}