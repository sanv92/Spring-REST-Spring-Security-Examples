package com.example.auth.web.controllers;

import com.example.auth.domain.User;
import com.example.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class UserController2 {

    private final UserRepository userRepository;

    @Autowired
    public UserController2(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<Resources<User>> getUsers() {
        List<User> users = userRepository.findAll();

        Resources<User> resources = new Resources<>(users);

        resources.add(linkTo(methodOn(UserController2.class).getUsers()).withSelfRel());

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") Long userId) {
        User user = userRepository.findByUsername("Sander").orElseGet(null);

        Resource<User> resource = new Resource<>(user);

        resource.add(linkTo(methodOn(UserController2.class).getUser(userId)).withSelfRel());

//        UserResourceAssembler assembler = new UserResourceAssembler();
//        UserResource resource = assembler.toResource(user);
        // List<PersonResource> resources = assembler.toResources(people);

        return ResponseEntity.ok(resource);
    }

    private List<Link> getSingleItemLinks(long id) {
        return Arrays.asList(
                linkTo(methodOn(UserController2.class).getUsers()).withRel("users")
        );

//        return Arrays.asList(linkTo(methodOn(UserController.class).findOne(id)).withSelfRel()
//                        .andAffordance(afford(methodOn(UserController.class).updateEmployee(null, id)))
//                        .andAffordance(afford(methodOn(UserController.class).deleteEmployee(id))),
//                linkTo(methodOn(UserController.class).getUsers()).withRel("users"));
    }
}
