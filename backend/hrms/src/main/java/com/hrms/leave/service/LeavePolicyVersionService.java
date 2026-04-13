package com.hrms.leave.service;

import com.hrms.leave.dto.CreateLeavePolicyVersionRequest;
import com.hrms.leave.dto.LeavePolicyVersionResponse;

public interface LeavePolicyVersionService {

    LeavePolicyVersionResponse createVersion(CreateLeavePolicyVersionRequest request);

}