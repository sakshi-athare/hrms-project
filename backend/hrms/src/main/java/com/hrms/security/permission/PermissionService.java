package com.hrms.security.permission;

import com.hrms.common.exception.UnauthorizedActionException;
import com.hrms.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermissionService {

    // Central method → single source of truth
    private List<Permission> getUserPermissions(User user) {
        return RolePermissionMapper.getPermissions(user.getRole());
    }

    // Strict check → throws exception
    public void check(User user, Permission permission) {

        List<Permission> permissions = getUserPermissions(user);

        if (!permissions.contains(permission)) {
            throw new UnauthorizedActionException(
                "You don't have permission: " + permission
            );
        }
    }

    // Soft check → returns boolean
    public boolean has(User user, Permission permission) {

        List<Permission> permissions = getUserPermissions(user);

        return permissions.contains(permission);
    }
}