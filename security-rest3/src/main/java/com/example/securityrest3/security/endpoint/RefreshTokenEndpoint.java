package com.example.securityrest3.security.endpoint;

import com.example.securityrest3.domain.User;
import com.example.securityrest3.security.config.SecurityProperties;
import com.example.securityrest3.security.details.MyUserDetails;
import com.example.securityrest3.security.dtos.TokenDto;
import com.example.securityrest3.security.model.token.AccessJwtToken;
import com.example.securityrest3.security.model.token.JwtTokenContext;
import com.example.securityrest3.security.model.token.JwtTokenFactory;
import com.example.securityrest3.security.model.token.RefreshJwtToken;
import com.example.securityrest3.security.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class RefreshTokenEndpoint {

    private static final class InvalidJwtToken extends RuntimeException {

        InvalidJwtToken() {
            super();
        }

        @Override
        public String getMessage() {
            return "Invalid JWT token";
        }
    }

    private final JwtTokenFactory jwtTokenFactory;

    private final UserRepository userRepository;

    @Autowired
    public RefreshTokenEndpoint(JwtTokenFactory jwtTokenFactory, UserRepository userRepository) {
        this.jwtTokenFactory = jwtTokenFactory;
        this.userRepository = userRepository;
    }

    @Transactional
    @PostMapping("/refresh-token")
    public TokenDto refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        final var accessJwtToken = new AccessJwtToken(refreshTokenRequest.getAccessToken());
        final var refreshJwtToken = new RefreshJwtToken(refreshTokenRequest.getRefreshToken());

        final var accessClaims = getAccessToken(accessJwtToken);
        final var refreshClaims = getRefreshToken(refreshJwtToken);
        if (!isAccessTokenExpired(accessJwtToken)) {
            throw new InvalidJwtToken();
        }

        final var refreshId = accessClaims.get("id", String.class);
        final var accessId = refreshClaims.get("id", String.class);
        if (!verifyUuid(accessId, refreshId)) {
            throw new InvalidJwtToken();
        }

        final var accessUsername = accessClaims.getSubject();
        final var refreshUsername = refreshClaims.getSubject();
        if (!verifyUsername(accessUsername, refreshUsername)) {
            throw new InvalidJwtToken();
        }

        final var user = userRepository.findByUsername(refreshUsername)
                .orElseThrow(InvalidJwtToken::new);

        if (!hasAuthorities(user)) {
            throw new InsufficientAuthenticationException("User has no roles assigned");
        }

        if (!verifyToken(user.getRefreshToken(), refreshJwtToken.getToken())) {
            throw new InvalidJwtToken();
        }

        final var tokenContext = new JwtTokenContext(
                user.getUsername(),
                new ArrayList<>(new MyUserDetails(user).getAuthorities())
        );

        final var accessToken = jwtTokenFactory.createAccessJwtToken(tokenContext).getToken();
        final var refreshToken = jwtTokenFactory.createRefreshToken(tokenContext).getToken();

        user.setRefreshToken(refreshToken);

        return new TokenDto(
                accessToken,
                refreshToken
        );
    }

    private Claims getAccessToken(AccessJwtToken accessJwtToken) {
        try {
            return jwtTokenFactory.parseClaims(accessJwtToken.getToken(), SecurityProperties.SECRET).getBody();
        } catch (JwtTokenFactory.TokenExpiredTokenException ex) {
            return ex.getBody();
        } catch (Exception ex) {
            throw new InvalidJwtToken();
        }
    }

    private Claims getRefreshToken(RefreshJwtToken refreshJwtToken) {
        return jwtTokenFactory.parseClaims(refreshJwtToken.getToken(), SecurityProperties.SECRET).getBody();
    }

    private boolean isAccessTokenExpired(AccessJwtToken accessJwtToken) {
        try {
            final var accessClaimsJws = jwtTokenFactory.parseClaims(accessJwtToken.getToken(), SecurityProperties.SECRET);
            final var time = System.currentTimeMillis();
            final var accessTokenTime = accessClaimsJws.getBody().getExpiration().getTime();
            final var diff = accessTokenTime - time;
            return diff <= 0;
        } catch (JwtTokenFactory.TokenExpiredTokenException ex) {
            return true;
        } catch (Exception ex) {
            throw new InvalidJwtToken();
        }
    }

    private boolean verifyUsername(String accessUsername, String refreshUsername) {
        return accessUsername.equals(refreshUsername);
    }

    private boolean verifyUuid(String accessId, String refreshId) {
        return refreshId.equals(accessId);
    }

    private boolean hasAuthorities(User user) {
        if (Optional.ofNullable(user.getRoles()).isPresent()) {
            return !user.getRoles().isEmpty();
        }

        return false;
    }

    private boolean verifyToken(String refreshToken, String payloadToken) {
        return Optional.ofNullable(refreshToken)
                .orElseThrow(InvalidJwtToken::new).equals(
                        Optional.ofNullable(payloadToken)
                                .orElseThrow(InvalidJwtToken::new)
                );
    }
}
