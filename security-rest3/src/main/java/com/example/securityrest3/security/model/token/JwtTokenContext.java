package com.example.securityrest3.security.model.token;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class JwtTokenContext {

    private final String username;
    private final List<GrantedAuthority> authorities;

    public JwtTokenContext(String username, List<GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    public String getUsername() {
        return username;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

}
