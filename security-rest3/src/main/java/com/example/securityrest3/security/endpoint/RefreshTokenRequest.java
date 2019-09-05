package com.example.securityrest3.security.endpoint;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class RefreshTokenRequest {
    @NotBlank
    private final String accessToken;

    @NotBlank
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefreshTokenRequest)) return false;
        RefreshTokenRequest that = (RefreshTokenRequest) o;
        return Objects.equals(accessToken, that.accessToken) &&
                Objects.equals(refreshToken, that.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, refreshToken);
    }

    @Override
    public String toString() {
        return "RefreshTokenRequest{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
