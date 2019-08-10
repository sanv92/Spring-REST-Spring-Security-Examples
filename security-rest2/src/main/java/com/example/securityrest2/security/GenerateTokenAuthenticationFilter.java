package com.example.securityrest2.security;

import com.example.securityrest2.domain.Token;
import com.example.securityrest2.domain.User;
import com.example.securityrest2.repositories.TokenRepository;
import com.example.securityrest2.security.details.MyUserDetails;
import com.example.securityrest2.security.dtos.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GenerateTokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    static final class UserMapperException extends RuntimeException {
        UserMapperException() {
            super();
        }

        @Override
        public String getMessage() {
            return "Cannot map json to User";
        }
    }

    private final TokenRepository tokenRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    public GenerateTokenAuthenticationFilter(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (!request.getMethod().equals(HttpMethod.POST.name())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        UsernamePasswordAuthenticationToken authRequest = this.getAuthRequest(request);
        this.setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    public void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication auth
    ) throws IOException {
        if (auth.isAuthenticated()) {
            MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
            User user = userDetails.getUser();
            Token token = tokenRepository.save(
                    new Token(
                            RandomStringUtils.random(10, true, true),
                            user
                    )
            );

            response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token.getValue());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(
                    mapper.writeValueAsString(
                            new UserDto(
                                    user.getUsername(),
                                    user.getEmail(),
                                    user.getStatus().name(),
                                    token.getValue(),
                                    user.getRoles()
                            )
                    )
            );

            return;
        }

        throw new AccessDeniedException("User not authorized");
    }

    private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) {
        try {
            User user = mapper.readValue(request.getInputStream(), User.class);

            String username = user.getUsername();
            String password = user.getPassword();

            if (username == null) {
                username = "";
            }
            if (password == null) {
                password = "";
            }

            return new UsernamePasswordAuthenticationToken(username, password);
        } catch (IOException ex) {
            throw new UserMapperException();
        }
    }

}
