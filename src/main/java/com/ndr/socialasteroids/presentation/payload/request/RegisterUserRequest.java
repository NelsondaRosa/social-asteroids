package com.ndr.socialasteroids.presentation.payload.request;

import com.ndr.socialasteroids.domain.entity.User;

import lombok.Data;

@Data
public class RegisterUserRequest
{
    private String username;
    private String email;
    private String password;

    public RegisterUserRequest(User user)
    {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public RegisterUserRequest(){}

    // public User toDomainEntity()
    // {
    //     return new User(this.username, this.email, this.password);
    // }
}
