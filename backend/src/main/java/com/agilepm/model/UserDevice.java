package com.agilepm.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user_devices")
public class UserDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String deviceId;

    @Column(nullable = false)
    private String deviceType;  // e.g., "MOBILE", "DESKTOP", "BROWSER"

    @Column(nullable = false)
    private String browserName;

    @Column(nullable = false)
    private String operatingSystem;

    @Column(nullable = false)
    private String ipAddress;

    @Column(nullable = false)
    private Instant lastLoginAt;

    @Column(nullable = false)
    private int loginAttempts = 0;

    @Column(nullable = false)
    private boolean trusted = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceStatus status = DeviceStatus.ACTIVE;

    // Enum for device status
    public enum DeviceStatus {
        ACTIVE,      // Normal device
        SUSPICIOUS,  // Unusual activity detected
        BLOCKED      // Blocked due to security concerns
    }

    // Constructors
    public UserDevice() {
        this.lastLoginAt = Instant.now();
    }

    public UserDevice(User user, String deviceId, String deviceType, 
                      String browserName, String operatingSystem, 
                      String ipAddress) {
        this.user = user;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.browserName = browserName;
        this.operatingSystem = operatingSystem;
        this.ipAddress = ipAddress;
        this.lastLoginAt = Instant.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Instant lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    // Helper methods
    public void incrementLoginAttempts() {
        this.loginAttempts++;
    }

    public void resetLoginAttempts() {
        this.loginAttempts = 0;
    }
}
