package com.example.securityrest2.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class GenerateTokenAuthentication extends UsernamePasswordAuthenticationToken {

    private String token;

    public GenerateTokenAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public GenerateTokenAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
