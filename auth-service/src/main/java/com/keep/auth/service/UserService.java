package com.keep.auth.service;

import com.keep.auth.dto.RegisterRequest;
import com.keep.auth.dto.UserResponse;
import com.keep.auth.entity.User;

public interface UserService {

    User registerNewUser(RegisterRequest request);

    User findByEmailOrThrow(String email);

    UserResponse toUserResponse(User user);
}
