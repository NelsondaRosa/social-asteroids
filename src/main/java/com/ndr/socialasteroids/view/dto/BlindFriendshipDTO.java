package com.ndr.socialasteroids.view.dto;

import lombok.Data;

@Data
public class BlindFriendshipDTO {
    private Long userId;
    private Long friendId;

    public BlindFriendshipDTO(){}
}
