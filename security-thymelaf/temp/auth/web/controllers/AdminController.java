package com.example.auth.web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/admin")
@RestController
public class AdminController {

    @GetMapping
    public List<Object> getAll2() {
        return Arrays.asList("test2", "test2", "test2");
    }

    @GetMapping("/{userId}")
    public Long getById2(@PathVariable("userId") Long userId) {
        return userId;
    }

}
