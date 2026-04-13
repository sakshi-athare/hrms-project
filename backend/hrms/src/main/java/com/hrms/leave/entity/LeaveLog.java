package com.hrms.leave.entity;

import com.hrms.common.entity.BaseEntity;
import com.hrms.common.enums.LeaveStatus;
import com.hrms.user.entity.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "leave_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_request_id", nullable = false)
    private LeaveRequest leave;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus action;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by")
    private User performedBy;

    @Column(length = 1000)
    private String note;

}