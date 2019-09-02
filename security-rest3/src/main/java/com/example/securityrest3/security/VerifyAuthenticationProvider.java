package com.example.securityrest3.security;

import com.example.securityrest3.domain.Role;
import com.example.securityrest3.domain.User;
import com.example.securityrest3.security.config.SecurityProperties;
import com.example.securityrest3.security.details.MyUserDetails;
import com.example.securityrest3.security.model.TokenAuthentication;
import com.example.securityrest3.security.model.token.JwtTokenFactory;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

public class VerifyAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(VerifyAuthenticationProvider.class);

    private static final class TokenNotFoundException extends RuntimeException {

        TokenNotFoundException() {
            super();
        }

        @Override
        public String getMessage() {
            return "Token not found";
        }
    }

    private final JwtTokenFactory jwtTokenFactory;

    public VerifyAuthenticationProvider(JwtTokenFactory jwtTokenFactory) {
        this.jwtTokenFactory = jwtTokenFactory;
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        final var result = new TokenAuthentication(
                principal,
                null,
                user.getAuthorities()
        );
        result.setDetails(authentication.getDetails());

        if (result.isAuthenticated()) {
            return result;
        }

        return null;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {

    }

    @Override
    public UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
        final var tokenAuthentication = (TokenAuthentication) authentication;
        final var rawToken = tokenAuthentication.getToken();

        try {
            final var jwsClaims = jwtTokenFactory.parseClaims(rawToken, SecurityProperties.SECRET);
            final var parsedUsername = jwsClaims.getBody().getSubject();

            if (parsedUsername.isEmpty()) {
                throw new TokenNotFoundException();
            }

            List<String> roles = jwsClaims.getBody().get("roles", List.class);
            List<Role> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .map(authority -> new Role(Role.Type.valueOfRole(authority.getAuthority())))
                    .collect(Collectors.toList());

            var user = new User();
            user.setUsername(parsedUsername);
            user.setRoles(authorities);

            return new MyUserDetails(user);
        } catch (ExpiredJwtException exception) {
            log.warn("Request to parse expired JWT : {} failed : {}", rawToken, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.warn("Request to parse unsupported JWT : {} failed : {}", rawToken, exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.warn("Request to parse invalid JWT : {} failed : {}", rawToken, exception.getMessage());
        } catch (SignatureException exception) {
            log.warn("Request to parse JWT with invalid signature : {} failed : {}", rawToken, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.warn("Request to parse empty or null JWT : {} failed : {}", rawToken, exception.getMessage());
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TokenAuthentication.class.equals(authentication);
    }
}