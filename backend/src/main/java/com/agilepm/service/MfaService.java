package com.agilepm.service;

import com.agilepm.model.MfaBackupCode;
import com.agilepm.model.User;
import com.agilepm.repository.MfaBackupCodeRepository;
import com.agilepm.repository.UserRepository;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
public class MfaService {

    @Value("${app.mfa.issuer}")
    private String issuer;

    @Value("${app.mfa.totp-algorithm}")
    private String algorithm;

    @Value("${app.mfa.totp-digits}")
    private int digits;

    @Value("${app.mfa.totp-period}")
    private int period;

    @Value("${app.mfa.backup-codes-count}")
    private int backupCodesCount;

    @Value("${app.mfa.backup-codes-validity-days}")
    private int backupCodesValidityDays;

    private final UserRepository userRepository;
    private final MfaBackupCodeRepository backupCodeRepository;
    private final AuditService auditService;

    @Autowired
    public MfaService(
        UserRepository userRepository,
        MfaBackupCodeRepository backupCodeRepository,
        AuditService auditService
    ) {
        this.userRepository = userRepository;
        this.backupCodeRepository = backupCodeRepository;
        this.auditService = auditService;
    }

    @Transactional
    public String generateMfaSecret(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate a new secret
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        String secret = secretGenerator.generate();

        // Update user's MFA secret
        user.setMfaSecret(secret);
        userRepository.save(user);

        return secret;
    }

    public String generateQrCode(String username, String secret) throws QrGenerationException {
        QrData data = new QrData.Builder()
            .label(username)
            .secret(secret)
            .issuer(issuer)
            .algorithm(algorithm)
            .digits(digits)
            .period(period)
            .build();

        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] qrCodeImage = generator.generate(data);
        return getDataUriForImage(qrCodeImage, generator.getImageMimeType());
    }

    @Transactional
    public boolean validateTotpCode(Long userId, String totpCode) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate TOTP code
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

        boolean isValid = verifier.isValidCode(user.getMfaSecret(), totpCode);

        // Log the authentication attempt
        auditService.logAuthenticationEvent(
            "MFA_VALIDATION", 
            "USER", 
            userId.toString(), 
            isValid ? AuditLog.AuditLogStatus.SUCCESS : AuditLog.AuditLogStatus.FAILURE
        );

        return isValid;
    }

    @Transactional
    public List<String> generateBackupCodes(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete existing backup codes
        backupCodeRepository.deleteByUser(user);

        // Generate new backup codes
        Random random = new Random();
        Instant expiryDate = Instant.now().plus(
            java.time.Duration.ofDays(backupCodesValidityDays)
        );

        List<MfaBackupCode> backupCodes = random.ints(backupCodesCount, 100000, 999999)
            .mapToObj(code -> new MfaBackupCode(
                user, 
                String.valueOf(code), 
                expiryDate
            ))
            .collect(Collectors.toList());

        backupCodeRepository.saveAll(backupCodes);

        return backupCodes.stream()
            .map(MfaBackupCode::getCode)
            .collect(Collectors.toList());
    }

    @Transactional
    public boolean validateBackupCode(Long userId, String backupCode) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<MfaBackupCode> optionalBackupCode = backupCodeRepository.findByUserAndCode(user, backupCode);

        if (optionalBackupCode.isPresent()) {
            MfaBackupCode mfaBackupCode = optionalBackupCode.get();

            // Check if backup code is valid and not expired
            if (!mfaBackupCode.isExpired()) {
                mfaBackupCode.setUsed(true);
                backupCodeRepository.save(mfaBackupCode);

                // Log successful backup code usage
                auditService.logAuthenticationEvent(
                    "MFA_BACKUP_CODE_USED", 
                    "USER", 
                    userId.toString(), 
                    AuditLog.AuditLogStatus.SUCCESS
                );

                return true;
            }
        }

        // Log failed backup code attempt
        auditService.logAuthenticationEvent(
            "MFA_BACKUP_CODE_FAILED", 
            "USER", 
            userId.toString(), 
            AuditLog.AuditLogStatus.FAILURE
        );

        return false;
    }

    @Transactional
    public void disableMfa(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Clear MFA secret and delete backup codes
        user.setMfaSecret(null);
        userRepository.save(user);
        backupCodeRepository.deleteByUser(user);

        // Log MFA disabling
        auditService.logAuthenticationEvent(
            "MFA_DISABLED", 
            "USER", 
            userId.toString(), 
            AuditLog.AuditLogStatus.SUCCESS
        );
    }
}
