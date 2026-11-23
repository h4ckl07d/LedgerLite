package com.h4ckl07d.ledgerlite.auth.dto;

import com.h4ckl07d.ledgerlite.user.User;

import java.util.UUID;

public record LoginResponse(
        String token,
        UUID id,
        String name,
        String email
) {

    public static LoginResponse from (User user, String token) {
        return new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail());
    }
}

