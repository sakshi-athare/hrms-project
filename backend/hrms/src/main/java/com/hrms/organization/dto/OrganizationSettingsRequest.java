package com.hrms.organization.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class OrganizationSettingsRequest {

	private LocalTime officeStartTime;

	private Integer graceMinutes;

	private BigDecimal fullDayHours;

	private Integer lateMarksForHalfDay;
}