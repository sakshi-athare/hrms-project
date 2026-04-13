package com.hrms.holiday.dto;

import com.hrms.common.enums.HolidayType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HolidayRequest {

    private String name;

    private LocalDate date;

    private String description;

    private HolidayType type;

    private Boolean optionalHoliday;
}