package com.hrms.user.dto;

public class UpdateUserRequest {

    private String username;
    private String email;
    private Boolean isActive;
    private Long managerId;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Long getManagerId() {
        return managerId;
    }
}