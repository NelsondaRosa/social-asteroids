package com.ndr.socialasteroids.business.DTOs;

import com.ndr.socialasteroids.domain.entity.User;

import lombok.Data;

@Data
public class UserDTO
{
    private Long id;
    private String username;
    private String email;


    public UserDTO(User user)
    {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}
