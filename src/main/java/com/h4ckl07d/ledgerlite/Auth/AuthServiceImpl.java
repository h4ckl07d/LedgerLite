package com.h4ckl07d.ledgerlite.Auth;

import com.h4ckl07d.ledgerlite.Auth.dto.LoginRequest;
import com.h4ckl07d.ledgerlite.Auth.dto.LoginResponse;
import com.h4ckl07d.ledgerlite.User.UserRepository;
import com.h4ckl07d.ledgerlite.User.UserService;
import com.h4ckl07d.ledgerlite.User.dto.request.CreateUserRequest;
import com.h4ckl07d.ledgerlite.User.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    UserService userService;
    UserRepository userRepository;

    @Override
    public UserResponse registerUser(CreateUserRequest request) {
    return userService.createUser(request);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        return null;
    }


}
