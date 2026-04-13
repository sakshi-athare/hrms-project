package com.hrms.leave.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class LeaveTypeResponse {

    private Long id;

    private String name;

    private Boolean paid;

    private Boolean active;

}