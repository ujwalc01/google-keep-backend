package com.keep.auth.security;

import com.keep.auth.entity.Role;
import com.keep.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final String secret;
    private final long accessTokenValidityMillis;
    private final long refreshTokenValidityMillis;

    private SecretKey secretKey;
    private JwtParser jwtParser;

    public JwtTokenProvider(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.access-token-expiration-ms}") long accessTokenValidityMillis,
            @Value("${security.jwt.refresh-token-expiration-ms}") long refreshTokenValidityMillis
    ) {
        this.secret = secret;
        this.accessTokenValidityMillis = accessTokenValidityMillis;
        this.refreshTokenValidityMillis = refreshTokenValidityMillis;
    }

    @PostConstruct
    void init() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);

        this.jwtParser = Jwts
                .parser()
                .verifyWith(secretKey)
                .build();
    }

    public long getAccessTokenValidityMillis() {
        return accessTokenValidityMillis;
    }

    public String generateAccessToken(User user) {
        return buildToken(user, accessTokenValidityMillis);
    }

    public String generateRefreshToken(User user) {
        return buildToken(user, refreshTokenValidityMillis);
    }

    private String buildToken(User user, long validityMillis) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(validityMillis);

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("uid", user.getId())
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            jwtParser.parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String getEmail(String token) {
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }
}
