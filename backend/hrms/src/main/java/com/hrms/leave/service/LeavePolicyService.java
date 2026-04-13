package com.hrms.leave.service;

import com.hrms.leave.dto.CreateLeavePolicyRequest;
import com.hrms.leave.dto.LeavePolicyResponse;

import java.util.List;

public interface LeavePolicyService {

    LeavePolicyResponse createPolicy(CreateLeavePolicyRequest request);

    List<LeavePolicyResponse> getPolicies();

}