package com.growwithin.backend;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmployeeSkill(
        @NotBlank String name,
        @NotNull VerificationStatus verificationStatus
) {
    public enum VerificationStatus {
        VERIFIED,
        UNVERIFIED
    }
}