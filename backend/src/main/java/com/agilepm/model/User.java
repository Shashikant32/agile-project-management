package com.agilepm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "email")
       })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 100)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "mfa_secret")
    private String mfaSecret;

    @Column(name = "mfa_enabled", columnDefinition = "boolean default false")
    private boolean mfaEnabled = false;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Enum for user roles
    public enum Role {
        ADMIN(
            Set.of(
                Permission.USER_READ, Permission.USER_WRITE, Permission.USER_DELETE,
                Permission.COMPANY_READ, Permission.COMPANY_WRITE, Permission.COMPANY_DELETE,
                Permission.PROJECT_READ, Permission.PROJECT_WRITE, Permission.PROJECT_DELETE, Permission.PROJECT_ASSIGN_MANAGER,
                Permission.TASK_READ, Permission.TASK_WRITE, Permission.TASK_DELETE, Permission.TASK_ASSIGN,
                Permission.SPRINT_READ, Permission.SPRINT_WRITE, Permission.SPRINT_DELETE,
                Permission.COMMENT_READ, Permission.COMMENT_WRITE, Permission.COMMENT_DELETE
            )
        ),
        PROJECT_MANAGER(
            Set.of(
                Permission.PROJECT_READ, Permission.PROJECT_WRITE, Permission.PROJECT_ASSIGN_MANAGER,
                Permission.TASK_READ, Permission.TASK_WRITE, Permission.TASK_ASSIGN,
                Permission.SPRINT_READ, Permission.SPRINT_WRITE,
                Permission.COMMENT_READ, Permission.COMMENT_WRITE
            )
        ),
        DEVELOPER(
            Set.of(
                Permission.PROJECT_READ,
                Permission.TASK_READ, Permission.TASK_WRITE,
                Permission.SPRINT_READ,
                Permission.COMMENT_READ, Permission.COMMENT_WRITE
            )
        ),
        QA(
            Set.of(
                Permission.PROJECT_READ,
                Permission.TASK_READ, Permission.TASK_WRITE,
                Permission.SPRINT_READ,
                Permission.COMMENT_READ, Permission.COMMENT_WRITE
            )
        ),
        STAKEHOLDER(
            Set.of(
                Permission.PROJECT_READ,
                Permission.TASK_READ,
                Permission.SPRINT_READ,
                Permission.COMMENT_READ
            )
        );

        private final Set<Permission> permissions;

        Role(Set<Permission> permissions) {
            this.permissions = permissions;
        }

        public Set<Permission> getPermissions() {
            return permissions;
        }

        public boolean hasPermission(Permission permission) {
            return permissions.contains(permission);
        }
    }

    // Enum for permissions
    public enum Permission {
        USER_READ, USER_WRITE, USER_DELETE,
        COMPANY_READ, COMPANY_WRITE, COMPANY_DELETE,
        PROJECT_READ, PROJECT_WRITE, PROJECT_DELETE, PROJECT_ASSIGN_MANAGER,
        TASK_READ, TASK_WRITE, TASK_DELETE, TASK_ASSIGN,
        SPRINT_READ, SPRINT_WRITE, SPRINT_DELETE,
        COMMENT_READ, COMMENT_WRITE, COMMENT_DELETE
    }

    // Constructors
    public User() {}

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getMfaSecret() {
        return mfaSecret;
    }

    public void setMfaSecret(String mfaSecret) {
        this.mfaSecret = mfaSecret;
    }

    public boolean isMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }
}
