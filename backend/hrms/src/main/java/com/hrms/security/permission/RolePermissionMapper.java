package com.hrms.security.permission;

import com.hrms.common.enums.Role;

import java.util.List;

import static com.hrms.security.permission.Permission.*;

public class RolePermissionMapper {

    public static List<Permission> getPermissions(Role role) {

        return switch (role) {

        case CEO -> List.of(
        	    USER_CREATE,
        	    USER_UPDATE,
        	    USER_VIEW,
        	    USER_VIEW_ALL,
        	    USER_DELETE,
        	    USER_ASSIGN_MANAGER,   
        	    USER_CHANGE_STATUS,    
        	    ROLE_CHANGE,
        	    ROLE_LOG_VIEW,
        	    DASHBOARD_VIEW,
        	    DASHBOARD_ADMIN_VIEW
        	);

        	case HR -> List.of(
        	    USER_CREATE,
        	    USER_UPDATE,
        	    USER_VIEW,
        	    USER_VIEW_ALL,
        	    USER_ASSIGN_MANAGER,   
        	    ROLE_LOG_VIEW,
        	    DASHBOARD_VIEW,
        	    ATTENDANCE_VIEW_ALL,
        	    ATTENDANCE_UPDATE,
        	    ATTENDANCE_MANUAL_CREATE
        	);


        	case MANAGER -> List.of(
        	    USER_VIEW,
        	    USER_VIEW_ALL,
        	    USER_UPDATE,
        	    DASHBOARD_VIEW
        	);

        	case EMPLOYEE -> List.of(
        	    USER_VIEW,
        	    DASHBOARD_VIEW,
        	    ATTENDANCE_CHECK_IN,
        	    ATTENDANCE_CHECK_OUT,
        	    ATTENDANCE_VIEW_SELF,
        	    ATTENDANCE_REGULARIZE
        	);
        };
    }
}