package com.example.securityrest3.security.model.token;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.UUID;

public class JwtTokenContext {

    private final String username;

    private final List<GrantedAuthority> authorities;

    private final String uuid;

    public JwtTokenContext(String username, List<GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
        this.uuid = UUID.randomUUID().toString();
    }

    public String getUsername() {
        return username;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getUuid() {
        return uuid;
    }
}
