package com.agilepm.controller;

import com.agilepm.dto.PasswordResetDTO;
import com.agilepm.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/password")
@Tag(name = "Password Reset", description = "Password reset and recovery endpoints")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @Autowired
    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/forgot")
    @Operation(summary = "Initiate password reset", description = "Send password reset link to user's email")
    public ResponseEntity<String> forgotPassword(
        @RequestParam String email
    ) {
        passwordResetService.initiatePasswordReset(email);
        return ResponseEntity.ok("Password reset link sent to your email");
    }

    @PostMapping("/reset")
    @Operation(summary = "Reset password", description = "Reset password using reset token")
    public ResponseEntity<String> resetPassword(
        @Valid @RequestBody PasswordResetDTO passwordResetDTO
    ) {
        // Validate that passwords match
        if (!passwordResetDTO.passwordsMatch()) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        passwordResetService.resetPassword(passwordResetDTO);
        return ResponseEntity.ok("Password reset successfully");
    }
}
