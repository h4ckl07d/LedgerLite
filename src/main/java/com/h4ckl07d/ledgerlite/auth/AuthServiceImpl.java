package com.h4ckl07d.ledgerlite.auth;

import com.h4ckl07d.ledgerlite.auth.dto.LoginRequest;
import com.h4ckl07d.ledgerlite.auth.dto.LoginResponse;
import com.h4ckl07d.ledgerlite.user.User;
import com.h4ckl07d.ledgerlite.user.UserRepository;
import com.h4ckl07d.ledgerlite.user.UserService;
import com.h4ckl07d.ledgerlite.user.dto.request.CreateUserRequest;
import com.h4ckl07d.ledgerlite.user.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    UserService userService;
    UserRepository userRepository;
    JwtService jwtService;
    AuthenticationManager authenticationManager;

    @Override
    public UserResponse registerUser(CreateUserRequest request) {
    return userService.createUser(request);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = jwtService.generateToken(user.getEmail());
        return LoginResponse.from(user, token);
    }
}

