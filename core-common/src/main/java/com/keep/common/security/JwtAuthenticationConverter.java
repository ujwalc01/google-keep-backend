package com.keep.common.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

@FunctionalInterface
public interface JwtAuthenticationConverter {

    Authentication convert(String token, Claims claims, HttpServletRequest request);
}
