package com.example.securityrest3.security.model.token;

public final class RefreshJwtToken implements JwtToken {

    private final String token;

    public RefreshJwtToken(final String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

}
