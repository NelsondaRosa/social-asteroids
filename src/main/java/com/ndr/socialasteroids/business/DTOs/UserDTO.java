package com.ndr.socialasteroids.business.DTOs;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.RepresentationModel;

import com.ndr.socialasteroids.domain.entity.User;
import com.ndr.socialasteroids.presentation.controller.FriendshipController;
import com.ndr.socialasteroids.presentation.controller.MatchController;
import com.ndr.socialasteroids.presentation.controller.UserController;
import com.ndr.socialasteroids.security.entities.UserSecurityInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserDTO extends RepresentationModel<UserDTO>
{
    private Long id;
    private String username;
    private String email;

    public UserDTO(User user)
    {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        //Self - matches - friends
        add(linkTo(methodOn(UserController.class).getUser(this.id.toString())).withSelfRel());
        add(linkTo(methodOn(MatchController.class).getMatches(this.id.toString())).withRel("matches"));
        add(linkTo(methodOn(FriendshipController.class).getFriends(this.id.toString())).withRel("friends"));
    }

    public UserDTO(Long id, String username, String email)
    {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public UserDTO(UserSecurityInfo userSecurity)
    {
        this.id = userSecurity.getId();
        this.username = userSecurity.getUsername();
        this.email = userSecurity.getEmail();
    }
}
