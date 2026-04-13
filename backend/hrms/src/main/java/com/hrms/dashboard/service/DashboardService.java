package com.hrms.dashboard.service;

import com.hrms.dashboard.dto.DashboardSummaryResponse;
import com.hrms.user.entity.User;

public interface DashboardService {

    DashboardSummaryResponse getSummary(User user);
}