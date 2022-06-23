package com.ndr.socialasteroids.security.entities;

import java.util.Set;

import com.ndr.socialasteroids.domain.entity.Role;
import com.ndr.socialasteroids.domain.entity.User;

import lombok.Data;

@Data
public class UserSecurityInfo
{
    private Long id;
    private String username;
    private String email;
    private String password;
    private Set<Role> roles;

    public UserSecurityInfo(User user)
    {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }

    public UserSecurityInfo(Long id, String username, String password, Set<Role> roles)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public UserSecurityInfo(Long id, String username, String password)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = null;
    }
}
