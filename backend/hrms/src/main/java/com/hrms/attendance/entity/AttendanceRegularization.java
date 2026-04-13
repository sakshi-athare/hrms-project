package com.hrms.attendance.entity;

import java.time.LocalDateTime;

import com.hrms.attendance.enums.RegularizationStatus;
import com.hrms.common.entity.BaseEntity;
import com.hrms.user.entity.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attendance_regularizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceRegularization extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Attendance attendance;

    private LocalDateTime requestedCheckIn;

    private LocalDateTime requestedCheckOut;

    @Column(length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegularizationStatus status;

    @ManyToOne
    private User reviewedBy;

    private String rejectionReason;

}