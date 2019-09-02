package com.example.securityrest3.web.controllers;

import com.example.securityrest3.services.UserService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/test5")
    public String getUsers5() {
        return usersService.getUsers5();
    }

    @GetMapping("/test6")
    public String getUsers6() {
        return usersService.getUsers6();
    }

    @GetMapping("/test7")
    public String getUsers7() {
        return usersService.getUsers7();
    }

    @PostMapping("/test7")
    public String createUsers7() {
        return usersService.getUsers7();
    }

    @GetMapping("/test/{userId}")
    public String getUsers7(@PathVariable("userId") Long userId) {
        return usersService.getUserById();
    }
}
