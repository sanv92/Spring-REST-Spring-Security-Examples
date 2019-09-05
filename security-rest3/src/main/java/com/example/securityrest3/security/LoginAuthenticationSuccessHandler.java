package com.example.securityrest3.security;

import com.example.securityrest3.domain.User;
import com.example.securityrest3.security.details.MyUserDetails;
import com.example.securityrest3.security.dtos.UserDto;
import com.example.securityrest3.security.model.token.JwtTokenContext;
import com.example.securityrest3.security.model.token.JwtTokenFactory;
import com.example.securityrest3.security.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    static final class UserNotFoundException extends ResourceNotFoundException {
        UserNotFoundException() {
            super();
        }

        @Override
        public String getMessage() {
            return "User not found";
        }
    }

    private final UserRepository userRepository;

    private final JwtTokenFactory tokenFactory;

    private final ObjectMapper mapper;

    public LoginAuthenticationSuccessHandler(UserRepository userRepository, JwtTokenFactory tokenFactory, ObjectMapper mapper) {
        this.userRepository = userRepository;
        this.tokenFactory = tokenFactory;
        this.mapper = mapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        final var userDetails = (MyUserDetails) authentication.getPrincipal();
        final var roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        final var tokenContext = new JwtTokenContext(userDetails.getUsername(), new ArrayList<>(userDetails.getAuthorities()));
        final var accessToken = tokenFactory.createAccessJwtToken(tokenContext);
        final var refreshToken = tokenFactory.createRefreshToken(tokenContext);

        final var user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(UserNotFoundException::new);
        user.setRefreshToken(refreshToken.getToken());
        userRepository.save(user);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(
                mapper.writeValueAsString(
                        new UserDto(
                                userDetails.getUsername(),
                                User.Status.ACTIVE.name(),
                                roles,
                                accessToken.getToken(),
                                refreshToken.getToken()
                        )
                )
        );

        this.clearAuthenticationAttributes(request);
    }

    /**
     * Removes temporary authentication-related data which may have been stored
     * in the session during the authentication process..
     *
     */
    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

}
