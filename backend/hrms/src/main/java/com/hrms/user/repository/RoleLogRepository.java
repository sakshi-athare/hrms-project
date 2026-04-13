package com.hrms.user.repository;

import com.hrms.user.dto.RoleLogResponse;
import com.hrms.user.entity.RoleLog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleLogRepository extends JpaRepository<RoleLog, Long> {
	
	List<RoleLog> findAllByOrderByCreatedAtDesc();

	List<RoleLog> findByUserIdOrderByCreatedAtDesc(Long id);
}