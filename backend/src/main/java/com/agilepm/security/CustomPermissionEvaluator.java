package com.agilepm.security;

import com.agilepm.model.User;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(
        Authentication authentication, 
        Object targetDomainObject, 
        Object permission
    ) {
        if (authentication == null || permission == null) {
            return false;
        }

        // Get the current user
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        // If the user is not found, deny access
        if (!(userDetails instanceof User)) {
            return false;
        }

        User user = (User) userDetails;

        // Convert permission to Permission enum
        Permission requiredPermission;
        try {
            requiredPermission = Permission.valueOf(permission.toString());
        } catch (IllegalArgumentException e) {
            return false;
        }

        // Check if the user's role has the required permission
        return user.getRole().hasPermission(requiredPermission);
    }

    @Override
    public boolean hasPermission(
        Authentication authentication, 
        Serializable targetId, 
        String targetType, 
        Object permission
    ) {
        // This method can be extended to check permissions based on specific resources
        // For now, we'll delegate to the other hasPermission method
        return hasPermission(authentication, null, permission);
    }
}
