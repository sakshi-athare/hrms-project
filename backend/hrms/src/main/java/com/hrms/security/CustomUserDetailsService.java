package com.hrms.security;

import com.hrms.security.permission.Permission;
import com.hrms.security.permission.RolePermissionMapper;
import com.hrms.user.entity.User;
import com.hrms.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // FIX: use constructor injection instead of @Autowired field injection
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // ROLE authority
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        // PERMISSION authorities
        List<Permission> permissions = RolePermissionMapper.getPermissions(user.getRole());
        for (Permission p : permissions) {
            authorities.add(new SimpleGrantedAuthority(p.name()));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}