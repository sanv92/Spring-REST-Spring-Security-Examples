package com.example.auth.hateos;

import org.springframework.hateoas.ResourceSupport;

import java.util.Objects;

public class UserResource extends ResourceSupport {

    private Long id;

    private String username;

    private String password;

    public UserResource() {
    }

    public UserResource(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserResource)) return false;
        if (!super.equals(o)) return false;
        UserResource that = (UserResource) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, username, password);
    }

    @Override
    public String toString() {
        return "UserResource{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
