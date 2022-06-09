package com.ndr.socialasteroids.merchant.payload.request;

import lombok.Data;

@Data
public class BlindFriendshipReq
{
    private Long userId;
    private Long friendId;

    public BlindFriendshipReq(){}
}
