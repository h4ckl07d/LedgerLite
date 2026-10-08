package com.h4ckl07d.ledgerlite.Auth;

import com.h4ckl07d.ledgerlite.User.dto.request.CreateUserRequest;
import com.h4ckl07d.ledgerlite.User.dto.response.UserResponse;

public interface AuthService {

    UserResponse registerUser(CreateUserRequest request);
}
