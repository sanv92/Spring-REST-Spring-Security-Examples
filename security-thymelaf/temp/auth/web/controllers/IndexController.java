package com.example.auth.web.controllers;

import com.example.auth.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class IndexController {

    @GetMapping
    public User getIndex() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Sander");
        user.setPassword("Sander");

        return user;
    }

}
