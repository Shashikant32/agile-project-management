package com.agilepm.service;

import com.agilepm.dto.PasswordResetDTO;
import com.agilepm.exception.TokenExpiredException;
import com.agilepm.model.PasswordResetToken;
import com.agilepm.model.User;
import com.agilepm.repository.PasswordResetTokenRepository;
import com.agilepm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Value("${app.password-reset-token-expiration}")
    private long passwordResetTokenExpirationMs;

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public PasswordResetService(
        UserRepository userRepository,
        PasswordResetTokenRepository passwordResetTokenRepository,
        PasswordEncoder passwordEncoder,
        EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public void initiatePasswordReset(String email) {
        // Find user by email
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Generate reset token
        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plusMillis(passwordResetTokenExpirationMs);

        // Create and save password reset token
        PasswordResetToken resetToken = new PasswordResetToken(token, user, expiryDate);
        passwordResetTokenRepository.save(resetToken);

        // Send password reset email
        String resetLink = generateResetLink(token);
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
    }

    @Transactional
    public void resetPassword(PasswordResetDTO passwordResetDTO) {
        // Find token
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(passwordResetDTO.getToken())
            .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        // Check token expiration
        if (resetToken.isExpired()) {
            throw new TokenExpiredException("Password reset token has expired");
        }

        // Get user and update password
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(passwordResetDTO.getNewPassword()));
        userRepository.save(user);

        // Mark token as used
        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }

    private String generateResetLink(String token) {
        // In a real-world scenario, this would be your frontend reset password URL
        return "http://localhost:3000/reset-password?token=" + token;
    }
}
