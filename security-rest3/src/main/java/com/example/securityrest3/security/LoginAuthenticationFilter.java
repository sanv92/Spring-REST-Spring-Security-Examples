package com.example.securityrest3.security;

import com.example.securityrest3.security.config.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    static final class UserMapperException extends RuntimeException {
        UserMapperException() {
            super();
        }

        @Override
        public String getMessage() {
            return "Cannot map json to User";
        }
    }

    private final AuthenticationManager authenticationManager;

    private final ObjectMapper mapper;

    public LoginAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper mapper) {
        this.authenticationManager = authenticationManager;
        this.mapper = mapper;

        setFilterProcessesUrl(SecurityProperties.LOGIN_URL);
    }

    @Override
    @Transactional
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (!request.getMethod().equals(HttpMethod.POST.name())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try {
            var loginRequest = mapper.readValue(request.getReader(), LoginRequest.class);

            if (loginRequest.getUsername().isBlank() || loginRequest.getPassword().isBlank()) {
                throw new AuthenticationServiceException("Username or Password not provided");
            }

            var authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            this.setDetails(request, authenticationToken);

            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException ex) {
            throw new UserMapperException();
        }
    }

}