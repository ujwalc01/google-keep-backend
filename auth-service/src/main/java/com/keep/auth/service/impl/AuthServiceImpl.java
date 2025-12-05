package com.keep.auth.service.impl;

import com.keep.auth.dto.AuthResponse;
import com.keep.auth.dto.LoginRequest;
import com.keep.auth.dto.RegisterRequest;
import com.keep.auth.entity.User;
import com.keep.auth.security.JwtTokenProvider;
import com.keep.auth.service.AuthService;
import com.keep.auth.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(
            UserService userService,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        User user = userService.registerNewUser(request);
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        long expiresIn = jwtTokenProvider.getAccessTokenValidityMillis();
        return new AuthResponse(accessToken, refreshToken, expiresIn);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException ex) {
            // Will be handled by GlobalExceptionHandler as BadCredentialsException
            throw ex;
        }

        User user = userService.findByEmailOrThrow(request.getEmail());
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        long expiresIn = jwtTokenProvider.getAccessTokenValidityMillis();
        return new AuthResponse(accessToken, refreshToken, expiresIn);
    }
}
