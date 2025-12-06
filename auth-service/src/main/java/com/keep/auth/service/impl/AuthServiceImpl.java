package com.keep.auth.service.impl;

import com.keep.auth.dto.AuthResponse;
import com.keep.auth.dto.LoginRequest;
import com.keep.auth.dto.RegisterRequest;
import com.keep.auth.entity.Role;
import com.keep.auth.entity.User;
import com.keep.auth.service.AuthService;
import com.keep.auth.service.UserService;
import com.keep.common.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        return createAuthResponse(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException ex) {
            // Let Spring / GlobalExceptionHandler deal with bad credentials
            throw ex;
        }

        User user = userService.findByEmailOrThrow(request.getEmail());
        return createAuthResponse(user);
    }

    private AuthResponse createAuthResponse(User user) {
        Map<String, Object> claims = new HashMap<>();

        if (user.getId() != null) {
            claims.put("uid", user.getId());
        }

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        claims.put("roles", roles);

        String accessToken = jwtTokenProvider.buildAccessToken(user.getEmail(), claims);
        String refreshToken = jwtTokenProvider.buildRefreshToken(user.getEmail(), claims);
        long expiresIn = jwtTokenProvider.getAccessTokenValidityMillis();

        return new AuthResponse(accessToken, refreshToken, expiresIn);
    }
}
