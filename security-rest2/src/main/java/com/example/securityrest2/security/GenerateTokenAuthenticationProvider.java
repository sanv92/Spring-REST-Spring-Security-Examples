package com.example.securityrest2.security;

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


public class GenerateTokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private static final String BAD_CREDENTIALS = "Bad credentials";

    public GenerateTokenAuthenticationProvider(
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        UsernamePasswordAuthenticationToken result = new GenerateTokenAuthentication(
                principal,
                authentication.getCredentials(),
                user.getAuthorities()
        );
        result.setDetails(authentication.getDetails());

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
        return !(username == null || username.equals("") || username.length() < SecurityConstants.USERNAME_MIN_SIZE);
    }

    private boolean isValidPassword(String password, Authentication authentication) {
        return !(authentication.getCredentials() == null || password == null || authentication.getCredentials().equals("") || password.equals("") || password.length() < SecurityConstants.PASSWORD_MIN_SIZE);
    }

}
