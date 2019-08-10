package com.example.securityrest2.web.controllers;

import com.example.securityrest2.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService usersService;

    public UserController(UserService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public String getUsers() {
        return usersService.getUsers();
    }

    @GetMapping("/test1")
    public String getUsers1() {
        return usersService.getUsers1();
    }

    @GetMapping("/test2")
    public String getUsers2() {
        return usersService.getUsers2();
    }

    @GetMapping("/test3")
    public String getUsers3() {
        return usersService.getUsers3();
    }

    @GetMapping("/test4")
    public String getUsers4() {
        return usersService.getUsers4();
    }
}
