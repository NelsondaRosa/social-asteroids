package com.ndr.socialasteroids.view.viewobject;

import com.ndr.socialasteroids.domain.entities.AppUser;

import lombok.Data;

@Data
public class UserVO {
    
    private Long id;
    private String username;

    public UserVO(AppUser user){
        this.id = user.getId();
        this.username = user.getUsername();
    }

}
