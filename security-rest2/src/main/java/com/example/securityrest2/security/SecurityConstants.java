package com.example.securityrest2.security;

public class SecurityConstants {

    public static final String SECRET = "SecretKeyToGenJWTs";

    public static final long EXPIRATION_TIME = 864_000_000; // 10 days

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER_STRING = "Authorization";

    public static final String LOGIN_URL = "/login";

    public static final String REGISTRATION_URL = "/registration";

    public static final int USERNAME_MIN_SIZE = 6;

    public static final int PASSWORD_MIN_SIZE = 6;

}
