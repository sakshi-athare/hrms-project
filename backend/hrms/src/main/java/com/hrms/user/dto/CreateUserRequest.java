package com.hrms.user.dto;

import com.hrms.common.enums.Role;
import jakarta.validation.constraints.*;

public class CreateUserRequest {

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotNull
    private Role role;

    private Long managerId;

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public Long getManagerId() { return managerId; }
}