package com.hrms.user.dto;

import com.hrms.common.enums.Role;
import java.util.List;

public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private Role role;
    private Boolean isActive;

    private Long managerId;
    private String managerName;

    // 🔥 NEW FIELD
    private List<String> permissions;

    public UserResponse(Long id,
                        String username,
                        String email,
                        Role role,
                        Boolean isActive,
                        Long managerId,
                        String managerName,
                        List<String> permissions) {

        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
        this.managerId = managerId;
        this.managerName = managerName;
        this.permissions = permissions;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public Boolean getIsActive() { return isActive; }
    public Long getManagerId() { return managerId; }
    public String getManagerName() { return managerName; }

    // 🔥 NEW GETTER
    public List<String> getPermissions() {
        return permissions;
    }
}