package com.hrms.leave.entity;

import java.math.BigDecimal;

import com.hrms.common.entity.BaseEntity;
import com.hrms.common.enums.LedgerTransactionType;
import com.hrms.user.entity.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "leave_ledger")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User employee;

    @ManyToOne
    private LeaveType leaveType;

    private BigDecimal amount;

    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private LedgerTransactionType transactionType;

    private String description;

    private Long referenceLeaveId;

    private Boolean isAnnualAllocation;
}