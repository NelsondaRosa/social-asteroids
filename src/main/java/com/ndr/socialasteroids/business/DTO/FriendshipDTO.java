package com.ndr.socialasteroids.business.DTO;
import com.ndr.socialasteroids.domain.entity.Friendship;

import lombok.Data;

@Data
public class FriendshipDTO {
    private UserDTO user;
    private UserDTO friend;

    public FriendshipDTO(Friendship friendship)
    {
        this.user = new UserDTO(friendship.getUser());
        this.friend = new UserDTO(friendship.getFriend());
    }
}
