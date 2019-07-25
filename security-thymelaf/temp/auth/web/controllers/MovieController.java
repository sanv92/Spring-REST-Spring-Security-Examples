package com.example.auth.web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/movies")
@RestController
public class MovieController {

    @GetMapping
    public List<Object> getAll() {
        return Arrays.asList("test", "test", "test");
    }

    @GetMapping("/{movieId}")
    public Long getById(@PathVariable("movieId") Long movieId) {
        return movieId;
    }

}
