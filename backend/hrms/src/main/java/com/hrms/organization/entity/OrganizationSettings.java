package com.hrms.organization.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "organization_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalTime officeStartTime;

    @Column(nullable = false)
    private Integer graceMinutes;

    @Column(nullable = false)
    private BigDecimal fullDayHours;

    @Column(nullable = false)
    private Integer lateMarksForHalfDay;
}