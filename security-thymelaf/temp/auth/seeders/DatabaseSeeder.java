package com.example.auth.seeders;

import com.example.auth.domain.Role;
import com.example.auth.domain.User;
import com.example.auth.repositories.RoleRepository;
import com.example.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


@Component
public class DatabaseSeeder {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public DatabaseSeeder(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
        user1.setPassword("password");
        user1.setEmail("sander@gmail.com");
        user1.setRoles(
                new HashSet<>(
                        Arrays.asList(
                                roles.get(0),
                                roles.get(1)
                         )
                )
        );

        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("Alex");
        user2.setPassword("password");
        user1.setEmail("alex@gmail.com");
        user2.setRoles(
                new HashSet<>(
                        Arrays.asList(
                                roles.get(0),
                                roles.get(1)
                        )
                )
        );

        userRepository.save(user2);
    }
}