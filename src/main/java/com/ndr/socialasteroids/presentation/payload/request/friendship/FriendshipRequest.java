package com.ndr.socialasteroids.presentation.payload.request.friendship;

import lombok.Data;

@Data
public class FriendshipRequest
{
    private Long userId;
    private Long friendId;

    public FriendshipRequest(){}
}
