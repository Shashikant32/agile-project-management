package com.agilepm.repository;

import com.agilepm.model.MfaBackupCode;
import com.agilepm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MfaBackupCodeRepository extends JpaRepository<MfaBackupCode, Long> {
    List<MfaBackupCode> findByUser(User user);
    Optional<MfaBackupCode> findByUserAndCode(User user, String code);
    void deleteByUser(User user);
}
