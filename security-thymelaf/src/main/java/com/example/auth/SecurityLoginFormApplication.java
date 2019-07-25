package com.example.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import javax.servlet.Filter;

@SpringBootApplication
public class SecurityLoginFormApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SecurityLoginFormApplication.class, args);
    }

    @Bean
    public Filter filter() {
        ShallowEtagHeaderFilter filter = new ShallowEtagHeaderFilter();
        return filter;
    }

}
