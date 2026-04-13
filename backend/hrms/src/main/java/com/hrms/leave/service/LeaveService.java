package com.hrms.leave.service;

import com.hrms.leave.dto.ApplyLeaveRequest;
import com.hrms.leave.dto.LeaveBalanceResponse;
import com.hrms.leave.dto.LeaveResponse;

import java.util.List;

public interface LeaveService {

    LeaveResponse applyLeave(Long userId, ApplyLeaveRequest request);

    LeaveResponse approveLeave(Long leaveId, Long managerId);

    LeaveResponse rejectLeave(Long leaveId, Long managerId, String reason);

    LeaveResponse cancelLeave(Long leaveId, Long userId);

    List<LeaveResponse> getMyLeaves(Long userId);

    List<LeaveResponse> getPendingLeaves(Long managerId);

    LeaveBalanceResponse getLeaveBalance(Long userId, Long leaveTypeId);

}