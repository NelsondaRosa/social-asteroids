package com.ndr.socialasteroids.view.dto;

import com.ndr.socialasteroids.domain.entities.AppUser;

import lombok.Data;

@Data
public class UserDTO {

    private String username;
    private String email;
    private String password;

    public UserDTO(AppUser user){
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public UserDTO(){}

    public AppUser toDomainEntity(){
        return new AppUser(this.username, this.email, this.password);
    }
}
