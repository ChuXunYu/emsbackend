package com.dne.ems.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record PasswordResetRequest(
        @NotEmpty(message = "Email cannot be empty.")
        @Email(message = "Invalid email format.")
        String email
) {}