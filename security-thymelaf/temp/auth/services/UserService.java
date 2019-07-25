package com.example.auth.services;

import com.example.auth.domain.Role;
import com.example.auth.domain.User;
import com.example.auth.web.dtos.UserDto;
import com.example.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public User registerNewUserAccount(UserDto accountDto) throws RuntimeException {

        if (emailExists(accountDto.getEmail())) {
            throw new RuntimeException("There is an account with that email address:  + accountDto.getEmail()");
        }

        User user = new User();
        user.setUsername(accountDto.getUsername());
        user.setPassword(accountDto.getPassword());
        user.setEmail(accountDto.getEmail());
        user.setRoles(
                new HashSet<>(
                        Arrays.asList(
                                new Role(Role.Type.ADMIN)
                        )
                )
            );

        return userRepository.save(user);
    }
    private boolean emailExists(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return true;
        }

        return false;
    }
}