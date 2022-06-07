package com.ndr.socialasteroids.view.responseobject;

import com.ndr.socialasteroids.domain.entities.Friendship;

import lombok.Data;

@Data
public class FriendshipRO {

    private UserRO user;
    private UserRO friend;
    private boolean accepted;

    public FriendshipRO(Friendship friendship){
        this.user = new UserRO(friendship.getUser());
        this.friend = new UserRO(friendship.getFriend());
        this.accepted = friendship.isAccepted();
    }
}
