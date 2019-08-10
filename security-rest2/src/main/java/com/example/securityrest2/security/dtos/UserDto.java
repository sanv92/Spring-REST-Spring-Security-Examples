package com.example.securityrest2.security.dtos;

import com.example.securityrest2.domain.Role;

import java.util.HashSet;
import java.util.Set;

public class UserDto {

    private String username;

    private String email;

    private String status;

    private String token;

    private Set<Role> roles;

    public UserDto(String username, String email, String status) {
        this.username = username;
        this.email = email;
        this.status = status;
        this.roles = new HashSet<>();
    }

    public UserDto(String username, String email, String status, String token, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.status = status;
        this.token = token;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
