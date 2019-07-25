package com.example.securityrest2.security.attempts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthenticationFailureListener
        implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final LoginAttemptService loginAttemptService;

    private final HttpServletRequest request;

    @Autowired
    public AuthenticationFailureListener(LoginAttemptService loginAttemptService, HttpServletRequest request) {
        this.loginAttemptService = loginAttemptService;
        this.request = request;
    }

    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        loginAttemptService.loginFailed(getClientIP());
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
