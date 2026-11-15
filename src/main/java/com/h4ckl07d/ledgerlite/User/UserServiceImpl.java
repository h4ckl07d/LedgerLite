package com.h4ckl07d.ledgerlite.User;

import com.h4ckl07d.ledgerlite.User.dto.request.CreateUserRequest;
import com.h4ckl07d.ledgerlite.User.dto.response.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        if(userRepository.existsByEmail(request.email())){
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setName(request.name());


        String hashedPassword = passwordEncoder.encode(request.password());
        user.setPassword_hash(hashedPassword);

        User response = userRepository.save(user);
        return UserResponse.from(response);
    }
}
