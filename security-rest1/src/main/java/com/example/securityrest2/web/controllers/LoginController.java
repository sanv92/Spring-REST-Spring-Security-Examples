package com.example.securityrest2.web.controllers;

import com.example.securityrest2.domain.Token;
import com.example.securityrest2.domain.User;
import com.example.securityrest2.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/login")
@RestController
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public Token showRegistrationForm(@RequestBody User user) {
        return loginService.login(user);
    }
}
