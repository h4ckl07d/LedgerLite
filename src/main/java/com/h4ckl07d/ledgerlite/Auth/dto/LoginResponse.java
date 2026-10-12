package com.h4ckl07d.ledgerlite.Auth.dto;

import com.h4ckl07d.ledgerlite.User.User;

public record LoginResponse(
        String token,
        Long id,
        String email
) {

    public static LoginResponse from (User user, String token) {
        return new LoginResponse(
                token,
                user.getId(),
                user.getEmail());
    }
}

