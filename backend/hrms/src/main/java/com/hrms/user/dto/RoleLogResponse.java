package com.hrms.user.dto;

import com.hrms.common.enums.Role;
import java.time.LocalDateTime;

public class RoleLogResponse {

    private Long id;

    private Long userId;
    private String userName;

    private Role oldRole;
    private Role newRole;

    private Long changedBy;
    private String changedByName;

    private LocalDateTime createdAt;

    public RoleLogResponse(
            Long id,
            Long userId,
            String userName,
            Role oldRole,
            Role newRole,
            Long changedBy,
            String changedByName,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.oldRole = oldRole;
        this.newRole = newRole;
        this.changedBy = changedBy;
        this.changedByName = changedByName;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
    public Role getOldRole() { return oldRole; }
    public Role getNewRole() { return newRole; }
    public Long getChangedBy() { return changedBy; }
    public String getChangedByName() { return changedByName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}