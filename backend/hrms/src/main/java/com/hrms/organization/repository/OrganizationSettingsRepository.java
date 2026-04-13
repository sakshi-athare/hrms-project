package com.hrms.organization.repository;


import com.hrms.organization.entity.OrganizationSettings;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationSettingsRepository extends JpaRepository<OrganizationSettings, Long> {
	
	Optional<OrganizationSettings> findFirstByOrderByIdAsc();
}