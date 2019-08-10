package com.example.securityrest2.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class VerifyTokenAuthentication extends UsernamePasswordAuthenticationToken {

    private String token;

    public VerifyTokenAuthentication(String token) {
        super(null, null);
        this.token = token;
    }

    public VerifyTokenAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public VerifyTokenAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getName() {
        return token;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
