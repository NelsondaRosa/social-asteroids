package com.ndr.socialasteroids.dealer.payload.response;

import com.ndr.socialasteroids.domain.entity.Friendship;

import lombok.Data;

@Data
public class FriendshipRes
{
    private AppUserRes user;
    private AppUserRes friend;
    private boolean accepted;

    public FriendshipRes(Friendship friendship)
    {
        this.user = new AppUserRes(friendship.getUser());
        this.friend = new AppUserRes(friendship.getFriend());
        this.accepted = friendship.isAccepted();
    }
}
