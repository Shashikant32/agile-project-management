package com.agilepm.service;

import com.agilepm.model.AuditLog;
import com.agilepm.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.List;

@Service
public class AuditService {

    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

    private final AuditLogRepository auditLogRepository;

    @Autowired
    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void logAuthenticationEvent(
        String action, 
        String entityType, 
        String entityId, 
        AuditLog.AuditLogStatus status
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "SYSTEM";
        String ipAddress = getCurrentRequestIpAddress();

        AuditLog auditLog = new AuditLog(
            username, 
            action, 
            entityType, 
            entityId, 
            "Authentication event", 
            ipAddress, 
            status
        );

        auditLogRepository.save(auditLog);
        logger.info("Audit Log: {} - {} - {}", action, entityType, status);
    }

    @Transactional
    public void logEntityEvent(
        String action, 
        String entityType, 
        String entityId, 
        String details, 
        AuditLog.AuditLogStatus status
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "SYSTEM";
        String ipAddress = getCurrentRequestIpAddress();

        AuditLog auditLog = new AuditLog(
            username, 
            action, 
            entityType, 
            entityId, 
            details, 
            ipAddress, 
            status
        );

        auditLogRepository.save(auditLog);
        logger.info("Audit Log: {} - {} - {} - {}", action, entityType, entityId, status);
    }

    public List<AuditLog> getAuditLogsByUsername(String username) {
        return auditLogRepository.findByUsername(username);
    }

    public List<AuditLog> getAuditLogsByEntityType(String entityType) {
        return auditLogRepository.findByEntityType(entityType);
    }

    public List<AuditLog> getAuditLogsBetweenDates(Instant start, Instant end) {
        return auditLogRepository.findByTimestampBetween(start, end);
    }

    private String getCurrentRequestIpAddress() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            return request.getRemoteAddr();
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }
}
