package com.agilepm.service;

import com.agilepm.model.AuditLog;
import com.agilepm.model.User;
import com.agilepm.model.UserDevice;
import com.agilepm.repository.UserDeviceRepository;
import com.agilepm.repository.UserRepository;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdaptiveAuthenticationService {

    @Value("${app.security.max-login-attempts}")
    private int maxLoginAttempts;

    @Value("${app.security.suspicious-login-threshold-hours}")
    private int suspiciousLoginThresholdHours;

    private final UserDeviceRepository userDeviceRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    @Autowired
    public AdaptiveAuthenticationService(
        UserDeviceRepository userDeviceRepository,
        UserRepository userRepository,
        AuditService auditService
    ) {
        this.userDeviceRepository = userDeviceRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    @Transactional
    public UserDevice registerOrUpdateDevice(User user, HttpServletRequest request) {
        // Parse user agent
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String deviceId = generateDeviceId(request);
        String ipAddress = getClientIpAddress(request);

        // Find existing device or create new
        Optional<UserDevice> existingDevice = userDeviceRepository
            .findByUserAndDeviceId(user, deviceId);

        UserDevice device = existingDevice.orElseGet(() -> 
            new UserDevice(
                user, 
                deviceId, 
                userAgent.getOperatingSystem().getDeviceType().getName(),
                userAgent.getBrowser().getName(),
                userAgent.getOperatingSystem().getName(),
                ipAddress
            )
        );

        // Update device details
        device.setLastLoginAt(Instant.now());
        device.resetLoginAttempts();

        // Save device
        return userDeviceRepository.save(device);
    }

    public boolean isDeviceSuspicious(UserDevice device) {
        // Check login attempts
        if (device.getLoginAttempts() > maxLoginAttempts) {
            device.setStatus(UserDevice.DeviceStatus.SUSPICIOUS);
            return true;
        }

        // Check login frequency
        Instant now = Instant.now();
        Instant suspiciousThreshold = now.minus(Duration.ofHours(suspiciousLoginThresholdHours));
        
        if (device.getLastLoginAt().isBefore(suspiciousThreshold)) {
            device.setStatus(UserDevice.DeviceStatus.SUSPICIOUS);
            return true;
        }

        return false;
    }

    @Transactional
    public void handleFailedLogin(User user, HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String deviceId = generateDeviceId(request);
        String ipAddress = getClientIpAddress(request);

        // Find or create device
        UserDevice device = userDeviceRepository
            .findByUserAndDeviceId(user, deviceId)
            .orElseGet(() -> 
                new UserDevice(
                    user, 
                    deviceId, 
                    userAgent.getOperatingSystem().getDeviceType().getName(),
                    userAgent.getBrowser().getName(),
                    userAgent.getOperatingSystem().getName(),
                    ipAddress
                )
            );

        // Increment login attempts
        device.incrementLoginAttempts();

        // Check if device becomes suspicious
        if (isDeviceSuspicious(device)) {
            auditService.logAuthenticationEvent(
                "SUSPICIOUS_LOGIN", 
                "USER", 
                user.getId().toString(), 
                AuditLog.AuditLogStatus.WARNING
            );
        }

        // Save updated device
        userDeviceRepository.save(device);
    }

    private String generateDeviceId(HttpServletRequest request) {
        // Generate a unique device identifier based on multiple factors
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String ipAddress = getClientIpAddress(request);
        
        return UUID.nameUUIDFromBytes(
            (ipAddress + 
             userAgent.getBrowser().getName() + 
             userAgent.getOperatingSystem().getName())
            .getBytes()
        ).toString();
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String[] IP_HEADERS = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_CLIENT_IP",
            "HTTP_X_CLUSTER_CLIENT_IP"
        };
        
        for (String header : IP_HEADERS) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0];
            }
        }
        
        return request.getRemoteAddr();
    }

    @Transactional(readOnly = true)
    public List<UserDevice> getUserDevices(User user) {
        return userDeviceRepository.findByUser(user);
    }

    @Transactional
    public void trustDevice(Long deviceId) {
        UserDevice device = userDeviceRepository.findById(deviceId)
            .orElseThrow(() -> new RuntimeException("Device not found"));
        
        device.setTrusted(true);
        device.setStatus(UserDevice.DeviceStatus.ACTIVE);
        userDeviceRepository.save(device);
    }

    @Transactional
    public void blockDevice(Long deviceId) {
        UserDevice device = userDeviceRepository.findById(deviceId)
            .orElseThrow(() -> new RuntimeException("Device not found"));
        
        device.setStatus(UserDevice.DeviceStatus.BLOCKED);
        userDeviceRepository.save(device);

        // Log device blocking
        auditService.logAuthenticationEvent(
            "DEVICE_BLOCKED", 
            "DEVICE", 
            deviceId.toString(), 
            AuditLog.AuditLogStatus.WARNING
        );
    }
}
