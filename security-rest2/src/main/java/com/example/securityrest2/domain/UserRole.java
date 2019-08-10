package com.example.securityrest2.domain;

import org.springframework.security.core.GrantedAuthority;


public enum UserRole implements GrantedAuthority {

    ADMIN(Code.ADMIN),
    USER(Code.USER);

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "authority='" + authority + '\'' +
                '}';
    }

    public static class Code {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String USER = "ROLE_USER";
    }
}

//public enum UserRole {
//    ADMIN("ROLE_ADMIN");
//
//    private String role;
//
//    UserRole(String role) {
//        this.role = role;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//
//
//    @Override
//    public String toString() {
//        return "UserRole{" +
//                "role='" + role + '\'' +
//                '}';
//    }
//}

//public enum  UserRole {
//    USER("ROLE_USER"),
//    ADMIN("ROLE_ADMIN");
//
//    private String role;
//
//    UserRole(String role) {
//        this.role = role;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//}
