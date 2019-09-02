package com.example.securityrest3.security;

import com.example.securityrest3.security.config.SecurityProperties;
import com.example.securityrest3.security.model.TokenAuthentication;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

public class LoginAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private static final String BAD_CREDENTIALS = "Bad credentials";

    public LoginAuthenticationProvider(
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        final var result = new TokenAuthentication(
                principal,
                authentication.getCredentials(),
                user.getAuthorities()
        );
        result.setDetails(authentication.getDetails());

        if (result.isAuthenticated()) {
            return result;
        }

        return result;
    }

    @Override
    protected void doAfterPropertiesSet() {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (isValidPassword(userDetails.getPassword(), authentication)) {
            throw new BadCredentialsException(BAD_CREDENTIALS);
        }

        if (!passwordEncoder.matches((String) authentication.getCredentials(), userDetails.getPassword())) {
            throw new BadCredentialsException(BAD_CREDENTIALS);
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
        if (isValidUsername(username)) {
            throw new BadCredentialsException(BAD_CREDENTIALS);
        }

        try {
            return userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException notFound) {
            throw notFound;
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }
    }

    private boolean isValidUsername(String username) {
        return !(username == null || username.equals("") || username.length() < SecurityProperties.USERNAME_MIN_SIZE);
    }

    private boolean isValidPassword(String password, Authentication authentication) {
        return !(authentication.getCredentials() == null || password == null || authentication.getCredentials().equals("") || password.equals("") || password.length() < SecurityProperties.PASSWORD_MIN_SIZE);
    }

}