package com.example.securityrest1.web.controllers;

import com.example.securityrest1.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class TestController {

    @GetMapping("/test")
    public User getIndex() {
        User user = new User();
        user.setUsername("test");

        return user;
    }
}
