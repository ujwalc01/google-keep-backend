package com.keep.auth.service;

import com.keep.auth.dto.AuthResponse;
import com.keep.auth.dto.LoginRequest;
import com.keep.auth.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
