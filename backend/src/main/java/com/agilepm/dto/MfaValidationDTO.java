package com.agilepm.dto;

import jakarta.validation.constraints.Size;

public class MfaValidationDTO {

    @Size(min = 6, max = 6, message = "TOTP code must be 6 digits")
    private String totpCode;

    @Size(min = 6, max = 6, message = "Backup code must be 6 digits")
    private String backupCode;

    // Constructors
    public MfaValidationDTO() {}

    public MfaValidationDTO(String totpCode, String backupCode) {
        this.totpCode = totpCode;
        this.backupCode = backupCode;
    }

    // Getters and Setters
    public String getTotpCode() {
        return totpCode;
    }

    public void setTotpCode(String totpCode) {
        this.totpCode = totpCode;
    }

    public String getBackupCode() {
        return backupCode;
    }

    public void setBackupCode(String backupCode) {
        this.backupCode = backupCode;
    }
}
