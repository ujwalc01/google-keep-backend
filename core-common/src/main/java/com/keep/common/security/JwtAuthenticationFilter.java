package com.keep.common.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationConverter converter;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   JwtAuthenticationConverter converter) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.converter = converter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (jwtTokenProvider.validateToken(token)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                Claims claims = jwtTokenProvider.getClaims(token);
                Authentication authentication = converter.convert(token, claims, request);

                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
