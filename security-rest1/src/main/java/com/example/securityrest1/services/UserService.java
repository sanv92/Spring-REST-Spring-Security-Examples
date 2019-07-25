package com.example.securityrest1.services;

import com.example.securityrest1.domain.Role;
import com.example.securityrest1.domain.User;
import com.example.securityrest1.repositories.RoleRepository;
import com.example.securityrest1.repositories.UserRepository;
import com.example.securityrest1.web.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class UserService {

    static final class UserAlreadyExistException extends RuntimeException {
        UserAlreadyExistException() {
            super();
        }

        @Override
        public String getMessage() {
            return "User already exists";
        }
    }

    static final class NotFoundRoleException extends RuntimeException {
        NotFoundRoleException() {
            super();
        }

        @Override
        public String getMessage() {
            return "Not found role";
        }
    }

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUserAccount(UserDto accountDto) {

        if (emailExists(accountDto.getEmail())) {
            throw new UserAlreadyExistException();
        }

        Role role = roleRepository.findByName(Role.Type.USER)
                .orElseThrow(NotFoundRoleException::new);

        User user = new User();
        user.setUsername(accountDto.getUsername());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setEmail(accountDto.getEmail());
        user.setStatus(User.Status.ACTIVE);
        user.setRoles(new HashSet<>(Arrays.asList(role)));

        return userRepository.save(user);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}