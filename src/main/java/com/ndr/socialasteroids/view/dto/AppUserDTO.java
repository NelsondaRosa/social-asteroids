package com.ndr.socialasteroids.view.dto;

import java.util.Set;

import com.ndr.socialasteroids.domain.entities.AppUser;
import com.ndr.socialasteroids.domain.entities.Role;

import lombok.Data;

@Data
public class AppUserDTO {
    private Long id;
    private String username;
    private String password;
    private Set<Role> roles;

    public AppUserDTO(AppUser appUser){
        this.id = appUser.getId();
        this.username = appUser.getUsername();
        this.password = appUser.getPassword();
        this.roles = appUser.getRoles();
    }

    public AppUserDTO(Long id, String username, String password, Set<Role> roles){
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public AppUserDTO(Long id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = null;
    }
}
