package com.hrms.attendance.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hrms.common.entity.BaseEntity;
import com.hrms.common.enums.AttendanceStatus;
import com.hrms.user.entity.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
	    name = "attendance",
	    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","date"}),
	    indexes = {
	        @Index(name = "idx_attendance_user_date", columnList = "user_id, date"),
	        @Index(name = "idx_attendance_date", columnList = "date")
	    }
	)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Attendance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    @Column(precision = 5, scale = 2)
    private BigDecimal totalWorkHours;

    private boolean isLate;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

}