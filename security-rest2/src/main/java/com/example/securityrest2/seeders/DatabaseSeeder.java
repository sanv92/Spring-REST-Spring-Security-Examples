package com.example.securityrest2.seeders;

import com.example.securityrest2.domain.Role;
import com.example.securityrest2.domain.User;
import com.example.securityrest2.repositories.RoleRepository;
import com.example.securityrest2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


@Component
public class DatabaseSeeder {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DatabaseSeeder(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void seed() {
        Role role1 = new Role();
        role1.setName(Role.Type.USER);
        roleRepository.save(role1);

        Role role2 = new Role();
        role2.setName(Role.Type.ADMIN);
        roleRepository.save(role2);

        List<Role> roles = roleRepository.findAll();

        User user1 = new User();
        user1.setUsername("Sander");
        user1.setPassword(passwordEncoder.encode("password"));
        user1.setEmail("sander@gmail.com");
        user1.setStatus(User.Status.ACTIVE);
        user1.setRoles(
                new HashSet<>(
                        Arrays.asList(
                                roles.get(0),
                                roles.get(1)
                         )
                )
        );

        User user2 = new User();
        user2.setUsername("Alex");
        user2.setPassword(passwordEncoder.encode("password"));
        user2.setEmail("alex@gmail.com");
        user2.setStatus(User.Status.ACTIVE);
        user2.setRoles(
                new HashSet<>(
                        Arrays.asList(
                                roles.get(0)
                        )
                )
        );

        User user3 = new User();
        user3.setUsername("Endreu");
        user3.setPassword(passwordEncoder.encode("password"));
        user3.setEmail("endreu@gmail.com");
        user3.setStatus(User.Status.ACTIVE);
        user3.setRoles(
                new HashSet<>(
                        Arrays.asList(
                                roles.get(0)
                        )
                )
        );

        userRepository.saveAll(Arrays.asList(user1, user2, user3));
    }
}