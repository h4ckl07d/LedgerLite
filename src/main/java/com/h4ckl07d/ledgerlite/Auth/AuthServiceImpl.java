package com.h4ckl07d.ledgerlite.Auth;

import com.h4ckl07d.ledgerlite.User.UserService;
import com.h4ckl07d.ledgerlite.User.dto.request.CreateUserRequest;
import com.h4ckl07d.ledgerlite.User.dto.response.UserResponse;

public class AuthServiceImpl implements AuthService {
    UserService userService;

    @Override
    public UserResponse registerUser(CreateUserRequest request) {
    return userService.createUser(request);
    }
}
