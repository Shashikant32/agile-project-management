package com.agilepm.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String entityType;

    @Column
    private String entityId;

    @Column
    private String details;

    @Column(nullable = false)
    private Instant timestamp;

    @Column
    private String ipAddress;

    @Enumerated(EnumType.STRING)
    private AuditLogStatus status;

    // Enum for log status
    public enum AuditLogStatus {
        SUCCESS,
        FAILURE,
        WARNING
    }

    // Constructors
    public AuditLog() {
        this.timestamp = Instant.now();
    }

    public AuditLog(String username, String action, String entityType, String entityId, 
                    String details, String ipAddress, AuditLogStatus status) {
        this.username = username;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.details = details;
        this.timestamp = Instant.now();
        this.ipAddress = ipAddress;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public AuditLogStatus getStatus() {
        return status;
    }

    public void setStatus(AuditLogStatus status) {
        this.status = status;
    }
}
