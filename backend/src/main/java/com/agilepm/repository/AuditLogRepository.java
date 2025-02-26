package com.agilepm.repository;

import com.agilepm.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface AuditLogRepository extends 
    JpaRepository<AuditLog, Long>, 
    JpaSpecificationExecutor<AuditLog> {

    List<AuditLog> findByUsername(String username);
    List<AuditLog> findByEntityType(String entityType);
    List<AuditLog> findByTimestampBetween(Instant start, Instant end);
    List<AuditLog> findByUsernameAndEntityType(String username, String entityType);
}
