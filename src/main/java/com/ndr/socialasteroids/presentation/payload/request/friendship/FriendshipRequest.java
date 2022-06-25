package com.ndr.socialasteroids.presentation.payload.request.friendship;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class FriendshipRequest
{
    @NotNull private Long userId;
    @NotNull private Long friendId;

    public FriendshipRequest(){}
}
