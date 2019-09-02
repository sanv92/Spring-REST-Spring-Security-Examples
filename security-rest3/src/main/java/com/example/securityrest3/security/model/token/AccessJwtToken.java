package com.example.securityrest3.security.model.token;

public final class AccessJwtToken implements JwtToken {

    private final String rawToken;

    public AccessJwtToken(final String token) {
        this.rawToken = token;
    }

    public String getToken() {
        return this.rawToken;
    }
}
