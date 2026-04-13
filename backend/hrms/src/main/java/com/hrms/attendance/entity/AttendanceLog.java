package com.hrms.attendance.entity;

import java.time.LocalDateTime;

import com.hrms.common.entity.BaseEntity;
import com.hrms.user.entity.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attendance_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceLog extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Attendance attendance;

    private String action;

    @ManyToOne
    private User performedBy;

    private String note;

}