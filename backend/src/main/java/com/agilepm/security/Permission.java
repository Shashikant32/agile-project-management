package com.agilepm.security;

public enum Permission {
    // User Permissions
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    USER_DELETE("user:delete"),

    // Company Permissions
    COMPANY_READ("company:read"),
    COMPANY_WRITE("company:write"),
    COMPANY_DELETE("company:delete"),

    // Project Permissions
    PROJECT_READ("project:read"),
    PROJECT_WRITE("project:write"),
    PROJECT_DELETE("project:delete"),
    PROJECT_ASSIGN_MANAGER("project:assign_manager"),

    // Task Permissions
    TASK_READ("task:read"),
    TASK_WRITE("task:write"),
    TASK_DELETE("task:delete"),
    TASK_ASSIGN("task:assign"),

    // Sprint Permissions
    SPRINT_READ("sprint:read"),
    SPRINT_WRITE("sprint:write"),
    SPRINT_DELETE("sprint:delete"),

    // Comment Permissions
    COMMENT_READ("comment:read"),
    COMMENT_WRITE("comment:write"),
    COMMENT_DELETE("comment:delete");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
