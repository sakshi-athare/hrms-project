package com.hrms.dashboard.serviceimpl;

import com.hrms.common.enums.LeaveStatus;
import com.hrms.dashboard.dto.DashboardSummaryResponse;
import com.hrms.dashboard.service.DashboardService;
import com.hrms.user.entity.User;
import com.hrms.user.repository.UserRepository;
import com.hrms.leave.repository.LeaveRequestRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    @Override
    public DashboardSummaryResponse getSummary(User user) {

        String role = user.getRole().name();

        if (role.equals("CEO") || role.equals("HR")) {
            return getAdminSummary();
        }

        return getEmployeeSummary(user);
    }

    private DashboardSummaryResponse getAdminSummary() {

        long total = userRepository.count();
        long active = userRepository.countByIsActiveTrue();
        long inactive = userRepository.countByIsActiveFalse();

        // ✅ FIXED (LocalDate → LocalDateTime)
        LocalDateTime startOfMonth = LocalDate.now()
                .withDayOfMonth(1)
                .atStartOfDay();

        long newJoinees = userRepository.countByCreatedAtAfter(startOfMonth);

        LocalDate today = LocalDate.now();

        long onLeave = leaveRequestRepository
                .countByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        LeaveStatus.APPROVED,
                        today,
                        today
                );

        long pendingLeaves = leaveRequestRepository.countByStatus(LeaveStatus.PENDING);

        return new DashboardSummaryResponse(
                total,
                active,
                inactive,
                newJoinees,
                onLeave,
                pendingLeaves
        );
    }

    private DashboardSummaryResponse getEmployeeSummary(User user) {

        long myPendingLeaves = leaveRequestRepository
                .countByEmployee_IdAndStatus(user.getId(), LeaveStatus.PENDING);

        long myApprovedLeaves = leaveRequestRepository
                .countByEmployee_IdAndStatus(user.getId(), LeaveStatus.APPROVED);

        return new DashboardSummaryResponse(
                0,
                0,
                0,
                0,
                myApprovedLeaves,
                myPendingLeaves
        );
    }
}