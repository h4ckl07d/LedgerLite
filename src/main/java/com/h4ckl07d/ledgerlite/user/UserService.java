package com.h4ckl07d.ledgerlite.user;

import com.h4ckl07d.ledgerlite.user.dto.request.CreateUserRequest;
import com.h4ckl07d.ledgerlite.user.dto.response.UserResponse;

public interface UserService {

     UserResponse createUser(CreateUserRequest request);
}
