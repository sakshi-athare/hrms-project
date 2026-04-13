package com.hrms.leave.entity;

import com.hrms.common.entity.BaseEntity;
import com.hrms.user.entity.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "leave_balances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveBalance extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "leave_type_id")
    private LeaveType leaveType;

    private Integer totalLeaves;

    private Integer usedLeaves;

    private Integer remainingLeaves;

}