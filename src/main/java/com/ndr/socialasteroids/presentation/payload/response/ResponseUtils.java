package com.ndr.socialasteroids.presentation.payload.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ndr.socialasteroids.domain.entity.Friendship;

@Component
public class ResponseUtils
{
    public static List<FriendshipRes> createFriendshipResponseList(List<Friendship> friends)
    {
        List<FriendshipRes> friendsRes = new ArrayList<FriendshipRes>();

        friends.forEach((f) -> friendsRes.add(new FriendshipRes(f)));

        return friendsRes;
    }
    
}
