package com.example.securityrest3.security.dtos;

import java.util.ArrayList;
import java.util.List;

public class UserDto {

    private String username;

    private String status;

    private List<String> roles;

    private String accessToken;

    private String refreshToken;

    public UserDto(String username, String status) {
        this.username = username;
        this.status = status;
        this.roles = new ArrayList<>();
    }

    public UserDto(String username, String status, List<String> roles) {
        this.username = username;
        this.status = status;
        this.roles = roles;
    }

    public UserDto(String username, String status, List<String> roles, String accessToken, String refreshToken) {
        this.username = username;
        this.status = status;
        this.roles = roles;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
