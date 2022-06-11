package com.ndr.socialasteroids.security;

import java.util.Set;

import com.ndr.socialasteroids.domain.entity.Role;
import com.ndr.socialasteroids.domain.entity.User;

import lombok.Data;

@Data
public class UserSecurityDTO
{
    private Long id;
    private String username;
    private String password;
    private Set<Role> roles;

    public UserSecurityDTO(User appUser)
    {
        this.id = appUser.getId();
        this.username = appUser.getUsername();
        this.password = appUser.getPassword();
        this.roles = appUser.getRoles();
    }

    public UserSecurityDTO(Long id, String username, String password, Set<Role> roles)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public UserSecurityDTO(Long id, String username, String password)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = null;
    }
}
