package com.h4ckl07d.ledgerlite.auth;

import com.h4ckl07d.ledgerlite.auth.dto.LoginRequest;
import com.h4ckl07d.ledgerlite.auth.dto.LoginResponse;
import com.h4ckl07d.ledgerlite.user.dto.request.CreateUserRequest;
import com.h4ckl07d.ledgerlite.user.dto.response.UserResponse;

public interface AuthService {

    UserResponse registerUser(CreateUserRequest request);

    LoginResponse login(LoginRequest request);
}
