package com.example.securityrest1.security.details;

import com.example.securityrest1.domain.User;
import com.example.securityrest1.repositories.UserRepository;
import com.example.securityrest1.security.attempts.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
public class MyUserDetailsService implements UserDetailsService {

    static final class BlockedException extends RuntimeException {
        BlockedException() {
            super();
        }

        @Override
        public String getMessage() {
            return "blocked";
        }
    }

    private final UserRepository userRepository;

    private final LoginAttemptService loginAttemptService;

    private final HttpServletRequest request;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository, LoginAttemptService loginAttemptService, HttpServletRequest request) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
        this.request = request;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String ip = getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new BlockedException();
        }

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + email);
        }

        return new MyUserDetails(user);
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}