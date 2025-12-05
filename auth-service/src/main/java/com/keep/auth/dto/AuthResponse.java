package com.keep.auth.dto;

public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private long expiresInMillis;

    public AuthResponse() {
    }

    public AuthResponse(String accessToken, String refreshToken, long expiresInMillis) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresInMillis = expiresInMillis;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public long getExpiresInMillis() {
        return expiresInMillis;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setExpiresInMillis(long expiresInMillis) {
        this.expiresInMillis = expiresInMillis;
    }
}
