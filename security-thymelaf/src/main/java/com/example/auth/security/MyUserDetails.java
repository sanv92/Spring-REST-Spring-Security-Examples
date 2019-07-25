package com.example.auth.security;

import com.example.auth.domain.Role;
import com.example.auth.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class MyUserDetails implements UserDetails {

    private User user;

    private Set<GrantedAuthority> authorities;;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    private Set<GrantedAuthority> getAuthorities(Set<Role> roles) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        for (Role role : roles) {
            authorities.add(
                    new SimpleGrantedAuthority(role.getName().name())
            );
        }

        return authorities;
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
        return !user.getStatus().equals(User.Status.BANNED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus().equals(User.Status.ACTIVE);
    }

    public User getUser() {
        return user;
    }
}
