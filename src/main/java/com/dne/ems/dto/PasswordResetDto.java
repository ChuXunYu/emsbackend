package com.dne.ems.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record PasswordResetDto(
        @NotEmpty(message = "Token cannot be empty.")
        String token,

        @NotEmpty(message = "New password cannot be empty.")
        @Size(min = 8, message = "Password must be at least 8 characters long.")
        String newPassword
) {}