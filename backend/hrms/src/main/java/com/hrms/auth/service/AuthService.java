package com.hrms.auth.service;

import com.hrms.auth.dto.LoginRequest;
import com.hrms.auth.dto.RegisterRequest;
import com.hrms.user.entity.User;

public interface AuthService {

    void register(RegisterRequest request);

    public User authenticate(LoginRequest request);
}
