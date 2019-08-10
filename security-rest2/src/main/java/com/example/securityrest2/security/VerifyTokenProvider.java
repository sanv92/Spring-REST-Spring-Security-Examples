package com.example.securityrest2.security;

import com.example.securityrest2.domain.Role;
import com.example.securityrest2.domain.Token;
import com.example.securityrest2.domain.User;
import com.example.securityrest2.repositories.TokenRepository;
import com.example.securityrest2.repositories.UserRepository;
import com.example.securityrest2.security.details.MyUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;

import javax.persistence.EntityNotFoundException;
import java.util.Set;


public class VerifyTokenProvider extends AbstractUserDetailsAuthenticationProvider {

    static final class TokenNotFoundException extends RuntimeException {
        TokenNotFoundException() {
            super();
        }

        @Override
        public String getMessage() {
            return "Token not found";
        }
    }

    private final UserDetailsService userDetailsService;

    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    public VerifyTokenProvider(
            UserDetailsService userDetailsService,
            TokenRepository tokenRepository,
            UserRepository userRepository
    ) {
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        UsernamePasswordAuthenticationToken result = new GenerateTokenAuthentication(
                principal,
                null,
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

    }

    @Override
    public UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
        Token token = tokenRepository.findByValue(authentication.getName())
                .orElseThrow(TokenNotFoundException::new);

        User user = userRepository.findById(token.getUser().getId())
                .orElseThrow(EntityNotFoundException::new);

        Set<Role> roles = userRepository.findUserWithRoles(token.getUser().getId());
        user.setRoles(roles);

        return new MyUserDetails(user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return VerifyTokenAuthentication.class.equals(authentication);
    }

}