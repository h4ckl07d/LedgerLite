package com.h4ckl07d.ledgerlite.User;

import com.h4ckl07d.ledgerlite.User.dto.request.CreateUserRequest;
import com.h4ckl07d.ledgerlite.User.dto.response.UserResponse;

public interface UserService {

     UserResponse createUser(CreateUserRequest request);
}
