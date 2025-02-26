package com.agilepm.controller;

import com.agilepm.dto.MfaSetupDTO;
import com.agilepm.dto.MfaValidationDTO;
import com.agilepm.service.MfaService;
import com.agilepm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/mfa")
@Tag(name = "Multi-Factor Authentication", description = "MFA setup and validation endpoints")
public class MfaController {

    private final MfaService mfaService;
    private final UserService userService;

    @Autowired
    public MfaController(MfaService mfaService, UserService userService) {
        this.mfaService = mfaService;
        this.userService = userService;
    }

    @PostMapping("/setup")
    @Operation(summary = "Setup MFA for user", description = "Generate MFA secret and QR code")
    public ResponseEntity<Map<String, String>> setupMfa() {
        // Get current user's ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userService.getUserByEmail(email).getId();

        // Generate MFA secret
        String secret = mfaService.generateMfaSecret(userId);

        // Generate QR code
        try {
            String qrCode = mfaService.generateQrCode(email, secret);

            // Generate backup codes
            List<String> backupCodes = mfaService.generateBackupCodes(userId);

            // Prepare response
            Map<String, String> response = new HashMap<>();
            response.put("secret", secret);
            response.put("qrCode", qrCode);
            response.put("backupCodes", String.join(",", backupCodes));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate MFA code", description = "Validate TOTP or backup code")
    public ResponseEntity<Map<String, Boolean>> validateMfaCode(
        @Valid @RequestBody MfaValidationDTO validationDTO
    ) {
        // Get current user's ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userService.getUserByEmail(email).getId();

        // Validate TOTP or backup code
        boolean isValid = validationDTO.getBackupCode() != null
            ? mfaService.validateBackupCode(userId, validationDTO.getBackupCode())
            : mfaService.validateTotpCode(userId, validationDTO.getTotpCode());

        // Prepare response
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", isValid);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/disable")
    @Operation(summary = "Disable MFA", description = "Disable multi-factor authentication")
    public ResponseEntity<Void> disableMfa() {
        // Get current user's ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userService.getUserByEmail(email).getId();

        // Disable MFA
        mfaService.disableMfa(userId);

        return ResponseEntity.noContent().build();
    }
}
