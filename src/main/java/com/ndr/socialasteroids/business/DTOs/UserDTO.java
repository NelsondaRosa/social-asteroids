package com.ndr.socialasteroids.business.DTOs;

import java.util.Set;
import java.util.stream.Collectors;

import com.ndr.socialasteroids.domain.entity.User;
import com.ndr.socialasteroids.security.UserSecurityDTO;

import lombok.Data;

@Data
//TODO remover roles
public class UserDTO
{
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;

    public UserDTO(Long id, String username, String email, Set<String> roles)
    {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public UserDTO(User user)
    {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    public UserDTO(UserSecurityDTO userSecurity)
    {
        this.id = userSecurity.getId();
        this.username = userSecurity.getUsername();
        this.email = userSecurity.getEmail();
        this.roles = 
            userSecurity
                .getRoles()
                .stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }
}
