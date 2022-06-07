package com.ndr.socialasteroids.dealer.payload.request;

import lombok.Data;

@Data
public class BlindFriendshipReq {
    private Long userId;
    private Long friendId;

    public BlindFriendshipReq(){}
}
