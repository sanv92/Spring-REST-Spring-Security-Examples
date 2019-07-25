package com.example.securityrest1.services;

import com.example.securityrest1.domain.Token;
import com.example.securityrest1.domain.User;
import com.example.securityrest1.repositories.TokenRepository;
import com.example.securityrest1.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginService(
            UserRepository userRepository,
            TokenRepository tokenRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Token login(User user) {
        User userCandidate = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        if (passwordEncoder.matches(user.getPassword(), userCandidate.getPassword())) {
            Token token = new Token();
            token.setUser(userCandidate);
            token.setValue(RandomStringUtils.random(10, true, true));

            return tokenRepository.save(token);
        }

        throw new BadCredentialsException("Password Not matches");
    }
}
