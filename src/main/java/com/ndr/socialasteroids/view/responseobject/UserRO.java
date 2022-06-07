package com.ndr.socialasteroids.view.responseobject;

import com.ndr.socialasteroids.domain.entities.AppUser;

import lombok.Data;

@Data
public class UserRO {
    
    private Long id;
    private String username;

    public UserRO(AppUser user){
        this.id = user.getId();
        this.username = user.getUsername();
    }

}
