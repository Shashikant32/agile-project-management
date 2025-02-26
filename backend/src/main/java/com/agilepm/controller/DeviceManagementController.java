package com.agilepm.controller;

import com.agilepm.model.User;
import com.agilepm.model.UserDevice;
import com.agilepm.service.AdaptiveAuthenticationService;
import com.agilepm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/devices")
@Tag(name = "Device Management", description = "Manage and monitor user devices")
public class DeviceManagementController {

    private final AdaptiveAuthenticationService adaptiveAuthenticationService;
    private final UserService userService;

    @Autowired
    public DeviceManagementController(
        AdaptiveAuthenticationService adaptiveAuthenticationService,
        UserService userService
    ) {
        this.adaptiveAuthenticationService = adaptiveAuthenticationService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get user devices", description = "Retrieve all devices associated with the current user")
    public ResponseEntity<List<UserDevice>> getUserDevices() {
        // Get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);

        // Retrieve user devices
        List<UserDevice> devices = adaptiveAuthenticationService.getUserDevices(user);
        return ResponseEntity.ok(devices);
    }

    @PostMapping("/{deviceId}/trust")
    @Operation(summary = "Trust a device", description = "Mark a device as trusted")
    public ResponseEntity<Void> trustDevice(@PathVariable Long deviceId) {
        adaptiveAuthenticationService.trustDevice(deviceId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{deviceId}/block")
    @Operation(summary = "Block a device", description = "Block a suspicious or unwanted device")
    public ResponseEntity<Void> blockDevice(@PathVariable Long deviceId) {
        adaptiveAuthenticationService.blockDevice(deviceId);
        return ResponseEntity.noContent().build();
    }
}
