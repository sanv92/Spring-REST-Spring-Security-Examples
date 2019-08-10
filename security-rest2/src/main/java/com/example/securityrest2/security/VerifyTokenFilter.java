package com.example.securityrest2.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.removeStart;

public class VerifyTokenFilter extends AbstractAuthenticationProcessingFilter {

    private static final String MISSING_TOKEN = "Missing Authentication Token";

    public VerifyTokenFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    public VerifyTokenFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    @Transactional
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        final String header = Optional.ofNullable(request.getHeader(SecurityConstants.HEADER_STRING))
                .orElse(request.getParameter("token"));

        if (!isValidToken(header)) {
            throw new BadCredentialsException(MISSING_TOKEN);
        }

        VerifyTokenAuthentication tokenAuthorization = this.getAuthentication(header);

        return this.getAuthenticationManager().authenticate(tokenAuthorization);
    }

    @Override
    protected void successfulAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain,
            final Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    private VerifyTokenAuthentication getAuthentication(String header) {
        final String tokenValue = Optional.ofNullable(header)
                .map(value -> removeStart(value, SecurityConstants.TOKEN_PREFIX))
                .map(String::trim)
                .orElseThrow(() -> new BadCredentialsException(MISSING_TOKEN));

        if (!hasToken(tokenValue)) {
            throw new BadCredentialsException(MISSING_TOKEN);
        }

        return new VerifyTokenAuthentication(tokenValue);
    }

    private boolean hasToken(String tokenValue) {
        return !(tokenValue == null || tokenValue.equals("") || tokenValue.length() <= 3);
    }

    private boolean isValidToken(String tokenValue) {
        return !(!hasToken(tokenValue) || !tokenValue.startsWith(SecurityConstants.TOKEN_PREFIX));
    }

}
