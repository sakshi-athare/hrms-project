package com.hrms.user.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hrms.user.dto.RoleLogResponse;
import com.hrms.user.repository.RoleLogRepository;
import com.hrms.user.service.RoleLogService;

@Service
public class RoleLogServiceImpl implements RoleLogService {

	@Autowired
	private RoleLogRepository roleLogRepository;

	@Override
	public List<RoleLogResponse> getRoleLogs() {

		return roleLogRepository.findAll().stream()
				.map(log -> new RoleLogResponse(log.getId(), log.getUser().getId(), log.getUser().getUsername(),
						log.getOldRole(), log.getNewRole(), log.getChangedBy().getId(),
						log.getChangedBy().getUsername(), log.getCreatedAt()))
				.toList();
	}
}