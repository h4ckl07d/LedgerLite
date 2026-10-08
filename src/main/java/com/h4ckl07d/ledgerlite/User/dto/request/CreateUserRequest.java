package com.h4ckl07d.ledgerlite.User.dto.request;

import com.h4ckl07d.ledgerlite.User.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank(message = "Name can not be blank")
        @Size(max = 100)
        String name,

        @NotBlank(message = "Email cannot be blank")
        @Email
        @Size(max = 255, message = "Email must not exceed  255 characters")
        String email,

        @NotBlank
        String password
) {
}
