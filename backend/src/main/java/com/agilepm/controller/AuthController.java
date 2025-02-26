package com.agilepm.controller;

import com.agilepm.dto.UserDTO;
import com.agilepm.exception.TokenRefreshException;
import com.agilepm.model.RefreshToken;
import com.agilepm.model.User;
import com.agilepm.security.JwtTokenProvider;
import com.agilepm.service.RefreshTokenService;
import com.agilepm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User authentication and registration endpoints")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthController(
        UserService userService,
        JwtTokenProvider tokenProvider,
        AuthenticationManager authenticationManager,
        RefreshTokenService refreshTokenService
    ) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Register a new user", description = "Create a new user account")
    public ResponseEntity<UserDTO> registerUser(
        @Valid @RequestBody UserDTO userDTO,
        @RequestParam(required = false) Long companyId
    ) {
        // If company ID is provided, assign user to the company
        if (companyId != null) {
            userDTO.setCompanyId(companyId);
        }

        UserDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and generate JWT token")
    public ResponseEntity<Map<String, String>> loginUser(
        @RequestParam String email, 
        @RequestParam String password
    ) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );

        // Set the authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        User user = userService.getUserByEmail(email);
        String jwt = tokenProvider.generateToken(user);

        // Generate refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        // Return token and user details
        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        response.put("refreshToken", refreshToken.getToken());
        response.put("userId", user.getId().toString());
        response.put("email", user.getEmail());
        response.put("role", user.getRole().name());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh JWT token", description = "Generate a new JWT token using a refresh token")
    public ResponseEntity<Map<String, String>> refreshToken(
        @RequestParam String refreshToken
    ) {
        // Find and verify the refresh token
        RefreshToken token = refreshTokenService.findByToken(refreshToken)
            .map(refreshTokenService::verifyExpiration)
            .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token not found"));

        // Generate new JWT token
        User user = token.getUser();
        String newJwt = tokenProvider.generateToken(user);

        // Return new tokens
        Map<String, String> response = new HashMap<>();
        response.put("token", newJwt);
        response.put("refreshToken", refreshToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Invalidate refresh token")
    public ResponseEntity<Void> logout(
        @RequestParam String refreshToken
    ) {
        // Delete the refresh token
        refreshTokenService.findByToken(refreshToken)
            .ifPresent(token -> refreshTokenService.deleteByUserId(token.getUser().getId()));

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile")
    @Operation(summary = "Get user profile", description = "Retrieve logged-in user's profile")
    public ResponseEntity<UserDTO> getUserProfile() {
        // In a real implementation, this would use the authenticated user's ID
        // For now, we'll throw an unsupported operation
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PutMapping("/profile")
    @Operation(summary = "Update user profile", description = "Update logged-in user's profile")
    public ResponseEntity<UserDTO> updateUserProfile(
        @Valid @RequestBody UserDTO userDTO
    ) {
        // In a real implementation, this would use the authenticated user's ID
        // For now, we'll throw an unsupported operation
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
