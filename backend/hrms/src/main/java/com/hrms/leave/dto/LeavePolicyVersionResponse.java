package com.hrms.leave.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class LeavePolicyVersionResponse {

    private Long id;

    private Long policyId;

    private Integer versionNumber;

    private Integer annualLimit;

    private Integer carryForwardLimit;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

}