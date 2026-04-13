package com.hrms.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hrms.user.dto.RoleLogResponse;
import com.hrms.user.repository.RoleLogRepository;


public interface RoleLogService {


	List<RoleLogResponse> getRoleLogs();
}