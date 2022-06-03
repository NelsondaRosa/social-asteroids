package com.ndr.socialasteroids.view.viewobject;

import com.ndr.socialasteroids.domain.entities.Friendship;

import lombok.Data;

@Data
public class FriendshipVO {

    private UserVO user;
    private UserVO friend;
    private boolean accepted;

    public FriendshipVO(Friendship friendship){
        this.user = new UserVO(friendship.getUser());
        this.friend = new UserVO(friendship.getFriend());
        this.accepted = friendship.isAccepted();
    }
}
