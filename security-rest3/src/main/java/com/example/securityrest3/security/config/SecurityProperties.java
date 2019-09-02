package com.example.securityrest3.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:security.properties")
public class SecurityProperties {

    public static String LOGIN_URL = "/login";
    @Value("${security.login.path}")
    public void setLoginUrl(String loginUrl) {
        LOGIN_URL = loginUrl;
    }

    public static String REGISTRATION_URL = "/registration";
    @Value("${security.registration.path}")
    public void setRegistrationUrl(String registrationUrl) {
        REGISTRATION_URL = registrationUrl;
    }

    // Signing key for HS512 algorithm
    // You can use the page http://www.allkeysgenerator.com/ to generate all kinds of keys
    public static String SECRET = "123456";
    @Value("${security.secret}")
    public void setSecret(String secret) {
        SECRET = secret;
    }

    // JWT token defaults
    public static final String TOKEN_HEADER = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static String TOKEN_TYPE = "JWT";
    @Value("${security.token.type}")
    public void setTokenType(String tokenType) {
        TOKEN_TYPE = tokenType;
    }

    public static final String TOKEN_ISSUER = "domain.com";

    public static final String TOKEN_AUDIENCE = "secure-app";

    public static long EXPIRATION_TIME = 864_000_000; // 10 days

    public static int USERNAME_MIN_SIZE = 1;
    @Value("${security.validation.min-size}")
    public void setUsernameMinSize(Integer size) {
        USERNAME_MIN_SIZE = size;
    }

    public static int PASSWORD_MIN_SIZE = 1;
    @Value("${security.validation.max-size}")
    public void setPasswordMinSize(Integer size) {
        PASSWORD_MIN_SIZE = size;
    }

    public SecurityProperties() {}
}
