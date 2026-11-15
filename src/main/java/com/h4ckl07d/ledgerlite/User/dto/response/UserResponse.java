package com.h4ckl07d.ledgerlite.User.dto.response;

import com.h4ckl07d.ledgerlite.User.Role;
import com.h4ckl07d.ledgerlite.User.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        LocalDateTime created_At,
        Role role
) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreated_At(),
                user.getRole()
        );
    }
}
