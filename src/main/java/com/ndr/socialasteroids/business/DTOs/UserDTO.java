package com.ndr.socialasteroids.business.DTOs;

import com.ndr.socialasteroids.domain.entity.User;

import lombok.Data;

@Data
public class UserDTO
{
    private Long id;
    private String username;
    private String email;
    private String matchesURI;
    //+++private String friendsURI friends;

    public UserDTO(User user)
    {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        //+++this.matches = user.getMatches();
        //+++this.friends = user.getFriends();
    }
}
