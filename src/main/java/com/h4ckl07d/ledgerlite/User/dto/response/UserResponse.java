package com.h4ckl07d.ledgerlite.User.dto.response;

import com.h4ckl07d.ledgerlite.User.User;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        LocalDateTime created_At
) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreated_At()
        );
    }
}
