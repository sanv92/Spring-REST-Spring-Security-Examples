package com.example.securityrest3.web.controllers;

import com.example.securityrest3.services.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public String getAdmin() {
        return adminService.getAdmin();
    }

    @GetMapping("/test1")
    public String getAdmin1() {
        return adminService.getAdmin1();
    }

    @GetMapping("/test2")
    public String getAdmin2() {
        return adminService.getAdmin2();
    }

    @GetMapping("/test3")
    public String getAdmin3() {
        return adminService.getAdmin3();
    }

    @GetMapping("/test4")
    public String getAdmin4() {
        return adminService.getAdmin4();
    }
}
