package com.example.securityrest3.security;

import com.example.securityrest3.security.model.TokenAuthentication;
import com.example.securityrest3.security.config.SecurityProperties;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class VerifyAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String MISSING_TOKEN = "Missing Authentication Token";

    public VerifyAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        final var token = Optional.ofNullable(request.getHeader(SecurityProperties.TOKEN_HEADER))
                .orElse(request.getParameter("token"));

        if (!isValidToken(token)) {
            throw new BadCredentialsException(MISSING_TOKEN);
        }

        return this.getAuthenticationManager().authenticate(
                this.getAuthentication(token)
        );
    }

    private TokenAuthentication getAuthentication(String header) {
        final var rewToken = Optional.ofNullable(header)
                .map(value -> header.substring(SecurityProperties.TOKEN_PREFIX.length()))
                .map(String::trim)
                .orElseThrow(() -> new BadCredentialsException(MISSING_TOKEN));

        if (!hasToken(rewToken)) {
            throw new BadCredentialsException(MISSING_TOKEN);
        }

        return new TokenAuthentication(rewToken);
    }

    @Override
    protected void successfulAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain,
            final Authentication authResult
    ) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    private boolean hasToken(String tokenValue) {
        return !(tokenValue == null || tokenValue.isEmpty() || tokenValue.isBlank() || tokenValue.length() <= 3);
    }

    private boolean isValidToken(String tokenValue) {
        return !(!hasToken(tokenValue) || !tokenValue.startsWith(SecurityProperties.TOKEN_PREFIX));
    }

}
