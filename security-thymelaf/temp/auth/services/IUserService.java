package com.example.auth.services;

import com.example.auth.domain.User;
import com.example.auth.web.dtos.UserDto;

public interface IUserService {
    User registerNewUserAccount(UserDto accountDto)
            throws RuntimeException;
}
