package com.hrms.leave.service;

import com.hrms.leave.dto.CreateLeaveTypeRequest;
import com.hrms.leave.dto.LeaveTypeResponse;

import java.util.List;

public interface LeaveTypeService {

    LeaveTypeResponse createLeaveType(CreateLeaveTypeRequest request);

    List<LeaveTypeResponse> getLeaveTypes();

}