package com.hrms.holiday.entity;

import com.hrms.common.entity.BaseEntity;
import com.hrms.common.enums.HolidayType;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "holidays",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"holiday_date","company_year"})
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Holiday extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(name = "holiday_date", nullable = false)
    private LocalDate date;

    @Column(name = "company_year", nullable = false)
    private Integer year;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HolidayType type;

    @Column(nullable = false)
    private Boolean optionalHoliday;

    @Column(nullable = false)
    private Boolean active;
}