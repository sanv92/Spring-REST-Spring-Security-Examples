package com.example.securityrest3.security.endpoint;

import com.example.securityrest3.security.config.SecurityProperties;
import com.example.securityrest3.security.details.MyUserDetailsService;
import com.example.securityrest3.security.dtos.TokenDto;
import com.example.securityrest3.security.model.UserContext;
import com.example.securityrest3.security.model.token.JwtTokenFactory;
import com.example.securityrest3.security.model.token.RawAccessJwtToken;
import com.example.securityrest3.security.repositories.TokenRepository;
import org.apache.tomcat.jni.Time;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class RefreshTokenEndpoint {

    private static final class InvalidJwtToken extends RuntimeException {
        private static final long serialVersionUID = -294671188037098603L;
    }

    private final JwtTokenFactory jwtTokenFactory;

    private final MyUserDetailsService userDetailsService;

    private final TokenRepository tokenRepository;

    public RefreshTokenEndpoint(JwtTokenFactory jwtTokenFactory, MyUserDetailsService userDetailsService, TokenRepository tokenRepository) {
        this.jwtTokenFactory = jwtTokenFactory;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
    }

    @PostMapping("/refresh-token")
    public TokenDto refreshToken(RefreshTokenRequest refreshTokenRequest) {
        final var rawAccessToken = new RawAccessJwtToken(refreshTokenRequest.getAccessToken());
        final var rawRefreshToken = new RawAccessJwtToken(refreshTokenRequest.getRefreshToken());

        if (!isAccessTokenExpired(rawAccessToken)) {
            throw new InvalidJwtToken();
        }

        final var refreshClaimsJws = jwtTokenFactory.parseClaims(rawRefreshToken.getToken(), SecurityProperties.SECRET);
        final var userDetails = userDetailsService.loadUserByUsername(refreshClaimsJws.getBody().getSubject());
        if (!hasAuthorities(userDetails)) {
            throw new InsufficientAuthenticationException("User has no roles assigned");
        }

        tokenRepository.findByValue(rawRefreshToken.getToken()).orElseThrow(InvalidJwtToken::new);

        final var userContext = new UserContext(
                userDetails.getUsername(),
                new ArrayList<>(userDetails.getAuthorities())
        );

        return new TokenDto(
                jwtTokenFactory.createAccessJwtToken(userContext).getToken(),
                jwtTokenFactory.createRefreshToken(userContext).getToken()
        );
    }

    private boolean isAccessTokenExpired(RawAccessJwtToken rawAccessJwtToken) {
        try {
            final var accessClaimsJws = jwtTokenFactory.parseClaims(rawAccessJwtToken.getToken(), SecurityProperties.SECRET);
            long accessTokenTime = accessClaimsJws.getBody().getExpiration().getTime();
            long diff = accessTokenTime - Time.now();
            return diff >= 0;
        } catch (RuntimeException ex) {
            return true;
        }
    }

    private boolean hasAuthorities(UserDetails userDetails) {
        if (userDetails.getAuthorities() == null) {
            return false;
        }

        return !userDetails.getAuthorities().isEmpty();
    }

}
