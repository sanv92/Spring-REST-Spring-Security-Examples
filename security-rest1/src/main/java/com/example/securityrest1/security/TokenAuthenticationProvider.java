package com.example.securityrest1.security;

import com.example.securityrest1.domain.Token;
import com.example.securityrest1.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private final TokenRepository tokenRepository;

    private final UserDetailsService userDetailsService;

    @Autowired
    public TokenAuthenticationProvider(
            @Qualifier("myUserDetailsService") UserDetailsService userDetailsService,
            TokenRepository tokenRepository
    ) {
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;

        Optional<Token> tokenCandidate = tokenRepository.findByValue(tokenAuthentication.getName());
        if (tokenCandidate.isPresent()) {
            Token token = tokenCandidate.get();
            String login = token.getUser().getEmail();

            UserDetails userDetails = userDetailsService.loadUserByUsername(login);
            tokenAuthentication.setUserDetails(userDetails);
            tokenAuthentication.setAuthenticated(true);

            return tokenAuthentication;
        } else throw new IllegalArgumentException("Bad token!!!");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TokenAuthentication.class.equals(authentication);
    }
}
