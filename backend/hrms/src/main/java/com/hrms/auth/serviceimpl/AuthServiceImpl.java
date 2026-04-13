package com.hrms.auth.serviceimpl;

import com.hrms.auth.dto.LoginRequest;
import com.hrms.auth.dto.RegisterRequest;
import com.hrms.auth.service.AuthService;
import com.hrms.common.enums.Role;
import com.hrms.common.exception.BadRequestException;
import com.hrms.user.entity.User;
import com.hrms.user.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (userRepository.count() == 0) {
            user.setRole(Role.CEO);
        } else {
            user.setRole(Role.EMPLOYEE);
        }

        userRepository.save(user);
    }

    @Override
    public User authenticate(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        return user; 
    }
}