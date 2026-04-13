package com.hrms.holiday.dto;

import com.hrms.common.enums.HolidayType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HolidayResponse {

    private Long id;

    private String name;

    private LocalDate date;

    private Integer year;

    private String description;

    private HolidayType type;

    private Boolean optionalHoliday;
}