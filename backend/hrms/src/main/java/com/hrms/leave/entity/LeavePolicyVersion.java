package com.hrms.leave.entity;

import java.time.LocalDate;

import com.hrms.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "leave_policy_versions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeavePolicyVersion extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private LeavePolicy policy;

    @Column(nullable = false)
    private Integer versionNumber;

    @Column(nullable = false)
    private Integer annualLimit;

    @Column(nullable = false)
    private Integer carryForwardLimit;

    @Column(nullable = false)
    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    @Column(nullable = false)
    private Boolean active;
}