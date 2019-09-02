package com.example.securityrest3.security.model.token;

import com.example.securityrest3.security.config.SecurityProperties;
import com.example.securityrest3.security.model.UserContext;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtTokenFactory {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenFactory.class);

    private static final class TokenExpiredTokenException extends AuthenticationException {

        private static final long serialVersionUID = -5959543783324224864L;

        private final String token;

        public TokenExpiredTokenException(String msg, String token) {
            super(msg);
            this.token = token;
        }

        public TokenExpiredTokenException(String token, String msg, Throwable t) {
            super(msg, t);
            this.token = token;
        }

        public String token() {
            return this.token;
        }
    }

    private static final class JwtTokenException extends RuntimeException {
        JwtTokenException(String message) {
            super(message);
        }
    }

    public AccessJwtToken createAccessJwtToken(UserContext userContext) {
        if (userContext.getUsername().isBlank()) {
            throw new JwtTokenException("Cannot create JWT Token without username");
        }

        if (Optional.ofNullable(userContext.getAuthorities()).isEmpty() || userContext.getAuthorities().isEmpty()) {
            throw new JwtTokenException("User doesn't have any privileges");
        }

        final var currentTime = LocalDateTime.now();

        final var roles = userContext.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        final var signingKey = SecurityProperties.SECRET.getBytes();
        final var token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", SecurityProperties.TOKEN_TYPE)
                .setIssuer(SecurityProperties.TOKEN_ISSUER)
                .setAudience(SecurityProperties.TOKEN_AUDIENCE)
                .setSubject(userContext.getUsername())
                .setExpiration(
                        Date.from(currentTime
                                .plusMinutes(1)
                                .atZone(ZoneId.systemDefault()).toInstant())
                )
                .claim("roles", roles)
                .compact();

        return new AccessJwtToken(token);
    }

    public JwtToken createRefreshToken(UserContext userContext) {
        if (userContext.getUsername().isBlank()) {
            throw new JwtTokenException("Cannot create JWT Token without username");
        }

        final var currentTime = LocalDateTime.now();

        final var roles = userContext.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        final var signingKey = SecurityProperties.SECRET.getBytes();
        final var token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", SecurityProperties.TOKEN_TYPE)
                .setIssuer(SecurityProperties.TOKEN_ISSUER)
                .setId(UUID.randomUUID().toString())
                .setAudience(SecurityProperties.TOKEN_AUDIENCE)
                .setSubject(userContext.getUsername())
                .setExpiration(
                        Date.from(currentTime
                                .plusDays(1)
                                .atZone(ZoneId.systemDefault()).toInstant())
                )
                .claim("roles", roles)
                .compact();

        return new AccessJwtToken(token);
    }

    public Jws<Claims> parseClaims(String token, String signingKey) {
        try {
            return Jwts.parser()
                    .setSigningKey(signingKey.getBytes()).parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SecurityException ex) {
            log.error("Invalid JWT Token", ex);
            throw new BadCredentialsException("Invalid JWT token: ", ex);
        } catch (ExpiredJwtException expiredEx) {
            log.info("JWT Token is expired", expiredEx);
            throw new TokenExpiredTokenException(token, "JWT Token expired", expiredEx);
        }
    }
}
