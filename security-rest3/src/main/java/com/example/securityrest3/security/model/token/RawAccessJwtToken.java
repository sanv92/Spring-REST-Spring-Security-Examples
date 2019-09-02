package com.example.securityrest3.security.model.token;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

public class RawAccessJwtToken implements JwtToken {

    public static class JwtExpiredTokenException extends AuthenticationException {
        private static final long serialVersionUID = -5959543783324224864L;

        private JwtToken token;

        public JwtExpiredTokenException(String msg) {
            super(msg);
        }

        public JwtExpiredTokenException(JwtToken token, String msg, Throwable t) {
            super(msg, t);
            this.token = token;
        }

        public String token() {
            return this.token.getToken();
        }
    }

    private static Logger logger = LoggerFactory.getLogger(RawAccessJwtToken.class);
            
    private String token;
    
    public RawAccessJwtToken(String token) {
        this.token = token;
    }

    public Jws<Claims> parseClaims(String signingKey) {
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SecurityException ex) {
            logger.error("Invalid JWT Token", ex);
            throw new BadCredentialsException("Invalid JWT token: ", ex);
        } catch (ExpiredJwtException expiredEx) {
            logger.info("JWT Token is expired", expiredEx);
            throw new JwtExpiredTokenException(this, "JWT Token expired", expiredEx);
        }
    }

    @Override
    public String getToken() {
        return token;
    }
}
