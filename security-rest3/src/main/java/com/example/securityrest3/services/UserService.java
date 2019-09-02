package com.example.securityrest3.services;

import com.example.securityrest3.security.model.AuthoritiesConstants;
import com.example.securityrest3.domain.Role;
import com.example.securityrest3.domain.User;
import com.example.securityrest3.security.repositories.RoleRepository;
import com.example.securityrest3.security.repositories.UserRepository;
import com.example.securityrest3.web.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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
        user.setRoles(Arrays.asList(role));

        return userRepository.save(user);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public String getUsers() {
        return "admin";
    }

    public String getUsers1() {
        return "admin-test1";
    }

    public String getUsers2() {
        return "admin-test2";
    }

    @Secured(AuthoritiesConstants.ADMIN)
    public String getUsers3() {
        return "admin-test3";
    }

    public String getUsers4() {
        return "admin-test4";
    }

    @Secured(AuthoritiesConstants.ADMIN)
    public String getUsers5() {
        return "admin-test5";
    }

    public String getUsers6() {
        return "admin-test6";
    }

    public String getUsers7() {
        return "admin-test7";
    }

    public String getUserById() {
        return "get-user-by-id";
    }
}