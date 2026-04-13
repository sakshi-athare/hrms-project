package com.hrms.organization.service;

import com.hrms.common.exception.BadRequestException;
import com.hrms.common.exception.ResourceNotFoundException;
import com.hrms.organization.dto.OrganizationSettingsRequest;
import com.hrms.organization.dto.OrganizationSettingsResponse;
import com.hrms.organization.entity.OrganizationSettings;
import com.hrms.organization.repository.OrganizationSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationSettingsService {

	private final OrganizationSettingsRepository repository;

	public OrganizationSettingsResponse createSettings(OrganizationSettingsRequest request) {

		if (repository.existsById(1L)) {
			throw new BadRequestException("Organization settings already exist");
		}

		validate(request);

		OrganizationSettings settings = OrganizationSettings.builder().id(1L) // 🔥 enforce single row system
				.officeStartTime(request.getOfficeStartTime()).graceMinutes(request.getGraceMinutes())
				.fullDayHours(request.getFullDayHours()).lateMarksForHalfDay(request.getLateMarksForHalfDay()).build();

		repository.save(settings);

		return mapToResponse(settings);
	}

	public OrganizationSettingsResponse getSettings() {

		OrganizationSettings settings = repository.findById(1L)
				.orElseThrow(() -> new ResourceNotFoundException("Organization settings not configured"));

		return mapToResponse(settings);
	}

	public OrganizationSettingsResponse updateSettings(OrganizationSettingsRequest request) {

		OrganizationSettings settings = repository.findById(1L)
				.orElseThrow(() -> new ResourceNotFoundException("Organization settings not configured"));

		validate(request);

		settings.setOfficeStartTime(request.getOfficeStartTime());
		settings.setGraceMinutes(request.getGraceMinutes());
		settings.setFullDayHours(request.getFullDayHours());
		settings.setLateMarksForHalfDay(request.getLateMarksForHalfDay());

		repository.save(settings);

		return mapToResponse(settings);
	}

	private void validate(OrganizationSettingsRequest request) {

		if (request.getGraceMinutes() < 0) {
			throw new BadRequestException("Grace minutes cannot be negative");
		}

		if (request.getFullDayHours().doubleValue() <= 0) {
			throw new BadRequestException("Full day hours must be positive");
		}

		if (request.getLateMarksForHalfDay() < 0) {
			throw new BadRequestException("Late marks cannot be negative");
		}
	}

	private OrganizationSettingsResponse mapToResponse(OrganizationSettings settings) {

		return OrganizationSettingsResponse.builder().id(settings.getId())
				.officeStartTime(settings.getOfficeStartTime()).graceMinutes(settings.getGraceMinutes())
				.fullDayHours(settings.getFullDayHours()).lateMarksForHalfDay(settings.getLateMarksForHalfDay())
				.build();
	}
}