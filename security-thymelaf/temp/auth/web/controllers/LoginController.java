package com.example.auth.web.controllers;

import com.example.auth.repositories.UserRepository;
import com.example.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;


@Controller
public class LoginController {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public LoginController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showRegistrationForm(WebRequest request, Model model) {
        return "login/index";
    }
}
