package com.ndr.socialasteroids.presentation.payload.request.user;

import com.ndr.socialasteroids.domain.entity.User;

import lombok.Data;

@Data
public class CreateUserRequest
{
    private String username;
    private String email;
    private String password;

    public CreateUserRequest(User user)
    {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public CreateUserRequest(){}
}
