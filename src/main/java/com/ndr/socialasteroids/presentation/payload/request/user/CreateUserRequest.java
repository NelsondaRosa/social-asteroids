package com.ndr.socialasteroids.presentation.payload.request.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.ndr.socialasteroids.domain.entity.User;

import lombok.Data;

@Data
public class CreateUserRequest
{
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Email(message = "Email isn't valid")
    @Pattern(regexp=".+@.+\\..+", message="Please provide a valid email address")
    private String email;
    
    @NotBlank
    @Size(min = 8)
    private String password;

    public CreateUserRequest(User user)
    {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public CreateUserRequest(){}
}
