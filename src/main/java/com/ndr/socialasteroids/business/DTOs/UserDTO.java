package com.ndr.socialasteroids.business.DTOs;

import com.ndr.socialasteroids.domain.entity.User;
import com.ndr.socialasteroids.security.entities.UserSecurityInfo;

import lombok.Data;

@Data
public class UserDTO
{
    private Long id;
    private String username;
    private String email;

    public UserDTO(Long id, String username, String email)
    {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public UserDTO(User user)
    {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    public UserDTO(UserSecurityInfo userSecurity)
    {
        this.id = userSecurity.getId();
        this.username = userSecurity.getUsername();
        this.email = userSecurity.getEmail();
    }
}
