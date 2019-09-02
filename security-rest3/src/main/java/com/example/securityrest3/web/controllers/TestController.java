package com.example.securityrest3.web.controllers;

import com.example.securityrest3.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/tests")
public class TestController {

    private final UserService usersService;

    public TestController(UserService usersService) {
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

    @GetMapping("/test/{testId}")
    public String getUsers7(@PathVariable("testId") Long testId) {
        return usersService.getUserById();
    }
}
