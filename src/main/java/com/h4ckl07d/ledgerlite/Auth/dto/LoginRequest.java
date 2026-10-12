package com.h4ckl07d.ledgerlite.Auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank
        @Email(message = "Email is required")
        @Size(min = 6, max = 255, message = "Email must not exceed 255 characters")
        String email,

        @NotBlank
        @Size(min = 6)
        String password
) {
}
