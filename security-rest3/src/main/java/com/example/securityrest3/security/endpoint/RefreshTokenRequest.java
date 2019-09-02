package com.example.securityrest3.security.endpoint;

public class RefreshTokenRequest {
    private final String accessToken;

    private final String refreshToken;

    public RefreshTokenRequest(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
