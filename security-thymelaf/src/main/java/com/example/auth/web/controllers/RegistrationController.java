package com.example.auth.web.controllers;

import com.example.auth.domain.User;
import com.example.auth.services.UserService;
import com.example.auth.web.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;


@Controller
@RequestMapping("")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());

        return "registration/index";
    }

    @PostMapping("/registration")
    public String registerUserAccount(
            @ModelAttribute("user") @Valid UserDto accountDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", accountDto);
            return "registration/index";
        }

        createUserAccount(accountDto);
        model.addAttribute("user", accountDto);
        return "registration/successRegister";
    }

    private User createUserAccount(UserDto accountDto) {
        try {
            return userService.registerNewUserAccount(accountDto);
        } catch (RuntimeException ex) {
            return null;
        }
    }
}
