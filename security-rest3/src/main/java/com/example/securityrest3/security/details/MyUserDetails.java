package com.example.securityrest3.security.details;

import com.example.securityrest3.domain.Role;
import com.example.securityrest3.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {

    private User user;

    private Set<GrantedAuthority> authorities;

    public MyUserDetails(User user) {
        this.user = user;
        this.authorities = initAuthorities(user);
    }

    private Set<GrantedAuthority> initAuthorities(User user) {
        if (user.getRoles() == null) {
            return new HashSet<>();
        }

        return this.getAuthorities(user.getRoles());
    }

    private Set<GrantedAuthority> getAuthorities(List<Role> roles) {
        return roles
                .stream()
                .map(authority -> new SimpleGrantedAuthority(
                        Role.Type.valueOf(authority.getName().name()).getAuthority()
                ))
                .collect(Collectors.toSet());
    }

    public String getRefreshToken() {
        return user.getRefreshToken();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "MyUserDetails{" +
                "user=" + user +
                ", authorities=" + authorities +
                '}';
    }
}
