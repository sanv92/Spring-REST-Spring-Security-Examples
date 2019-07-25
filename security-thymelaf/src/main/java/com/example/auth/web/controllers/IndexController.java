package com.example.auth.web.controllers;

import com.example.auth.domain.User;
import com.example.auth.security.MyUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class IndexController {

    @GetMapping
    public String getIndex(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        MyUserDetails details = (MyUserDetails) authentication.getPrincipal();
        User user = details.getUser();

        if (authentication.isAuthenticated()) {
            model.addAttribute("user", user);
        }

        return "home/index";
    }

}
