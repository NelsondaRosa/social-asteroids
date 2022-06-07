package com.ndr.socialasteroids.dealer.payload.request;

import java.util.Set;

import com.ndr.socialasteroids.domain.entity.AppUser;
import com.ndr.socialasteroids.domain.entity.Role;

import lombok.Data;

@Data
public class AppUserReq {
    private Long id;
    private String username;
    private String password;
    private Set<Role> roles;

    public AppUserReq(AppUser appUser){
        this.id = appUser.getId();
        this.username = appUser.getUsername();
        this.password = appUser.getPassword();
        this.roles = appUser.getRoles();
    }

    public AppUserReq(Long id, String username, String password, Set<Role> roles){
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public AppUserReq(Long id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = null;
    }
}
