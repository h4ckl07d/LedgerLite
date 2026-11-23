package com.h4ckl07d.ledgerlite.auth;


import com.h4ckl07d.ledgerlite.auth.dto.LoginRequest;
import com.h4ckl07d.ledgerlite.auth.dto.LoginResponse;
import com.h4ckl07d.ledgerlite.user.dto.request.CreateUserRequest;
import com.h4ckl07d.ledgerlite.user.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    AuthServiceImpl authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody CreateUserRequest request) {

       UserResponse response = authService.registerUser(request);

       return ResponseEntity
               .status(HttpStatus.CREATED)
               .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}
