package com.example.securityrest3.services;

import com.example.securityrest3.security.model.AuthoritiesConstants;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    public String getAdmin() {
        return "admin";
    }

    public String getAdmin1() {
        return "admin-test1";
    }

    public String getAdmin2() {
        return "admin-test2";
    }

    @Secured(AuthoritiesConstants.ADMIN)
    public String getAdmin3() {
        return "admin-test3";
    }

    public String getAdmin4() {
        return "admin-test4";
    }
}
