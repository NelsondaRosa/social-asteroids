package com.ndr.socialasteroids.business.DTO;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.RepresentationModel;

import com.ndr.socialasteroids.domain.entity.Friendship;
import com.ndr.socialasteroids.presentation.controller.UserController;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FriendshipDTO extends RepresentationModel<FriendshipDTO>  {
    private UserDTO user;
    private UserDTO friend;

    public FriendshipDTO(Friendship friendship)
    {
        this.user = new UserDTO(friendship.getUser());
        this.friend = new UserDTO(friendship.getFriend());
        //user - friend
        add(linkTo(methodOn(UserController.class).getUserById(user.getId().toString())).withRel("user"));
        add(linkTo(methodOn(UserController.class).getUserById(friend.getId().toString())).withRel("friend"));
    }
}
