package com.h4ckl07d.ledgerlite.Auth;

import com.h4ckl07d.ledgerlite.Auth.dto.LoginRequest;
import com.h4ckl07d.ledgerlite.Auth.dto.LoginResponse;
import com.h4ckl07d.ledgerlite.User.dto.request.CreateUserRequest;
import com.h4ckl07d.ledgerlite.User.dto.response.UserResponse;

public interface AuthService {

    UserResponse registerUser(CreateUserRequest request);

    LoginResponse login(LoginRequest request);
}
