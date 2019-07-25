package com.example.auth.hateos;

import com.example.auth.web.controllers.UserController;
import com.example.auth.domain.User;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class UserResourceAssembler extends ResourceAssemblerSupport<User, UserResource> {

    public UserResourceAssembler() {
        super(UserController.class, UserResource.class);
    }

    @Override
    public UserResource toResource(User user) {

        UserResource resource = this.toResource(user);

        return resource;
    }
}