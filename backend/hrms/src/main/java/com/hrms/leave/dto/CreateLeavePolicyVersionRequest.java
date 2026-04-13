package com.hrms.leave.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateLeavePolicyVersionRequest {

    private Long policyId;

    private Integer annualLimit;

    private Integer carryForwardLimit;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

}