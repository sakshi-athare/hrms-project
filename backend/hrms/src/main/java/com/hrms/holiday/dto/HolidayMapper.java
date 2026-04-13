package com.hrms.holiday.dto;


import com.hrms.holiday.entity.Holiday;

public class HolidayMapper {

    public static HolidayResponse toDto(Holiday holiday) {

        return HolidayResponse.builder()
                .id(holiday.getId())
                .name(holiday.getName())
                .date(holiday.getDate())
                .year(holiday.getYear())
                .description(holiday.getDescription())
                .type(holiday.getType())
                .optionalHoliday(holiday.getOptionalHoliday())
                .build();
    }
}