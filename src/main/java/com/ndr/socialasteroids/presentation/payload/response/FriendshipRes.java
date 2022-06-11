package com.ndr.socialasteroids.presentation.payload.response;

import com.ndr.socialasteroids.business.DTOs.UserDTO;
import com.ndr.socialasteroids.domain.entity.Friendship;

import lombok.Data;

@Data
public class FriendshipRes
{
    private UserDTO user;
    private UserDTO friend;
    private boolean accepted;

    public FriendshipRes(Friendship friendship)
    {
        //+++this.user = new AppUserRes(friendship.getUser());
        //+++this.friend = new AppUserRes(friendship.getFriend());
        this.accepted = friendship.isAccepted();
    }
}
