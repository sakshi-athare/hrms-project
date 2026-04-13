package com.hrms.organization.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@Builder
public class OrganizationSettingsResponse {

    private Long id;

    private LocalTime officeStartTime;

    private Integer graceMinutes;

    private BigDecimal fullDayHours;

    private Integer lateMarksForHalfDay;
}