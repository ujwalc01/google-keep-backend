package com.keep.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.access-token-expiration-ms:3600000}")
    private long accessTokenValidityMillis;

    @Value("${security.jwt.refresh-token-expiration-ms:604800000}")
    private long refreshTokenValidityMillis;

    private SecretKey secretKey;
    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    public long getAccessTokenValidityMillis() {
        return accessTokenValidityMillis;
    }

    public long getRefreshTokenValidityMillis() {
        return refreshTokenValidityMillis;
    }

    public String buildAccessToken(String subject, Map<String, Object> claims) {
        return buildToken(subject, claims, accessTokenValidityMillis);
    }

    public String buildRefreshToken(String subject, Map<String, Object> claims) {
        return buildToken(subject, claims, refreshTokenValidityMillis);
    }

    public String buildToken(String subject, Map<String, Object> claims, long validityMillis) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(validityMillis);

        return Jwts.builder()
                .subject(subject)
                .claims(claims)
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

    public Claims getClaims(String token) {
        return jwtParser.parseSignedClaims(token).getPayload();
    }
}
