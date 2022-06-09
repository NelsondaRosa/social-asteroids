package com.ndr.socialasteroids.dealer.payload.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ndr.socialasteroids.domain.entity.Friendship;
import com.ndr.socialasteroids.domain.entity.Match;

@Component
public class ResponseUtils
{
    public static List<FriendshipRes> createFriendshipResponseList(List<Friendship> friends)
    {
        List<FriendshipRes> friendsRes = new ArrayList<FriendshipRes>();

        friends.forEach((f) -> friendsRes.add(new FriendshipRes(f)));

        return friendsRes;
    }

    public static List<MatchRes> createMatchResponseList(List<Match> matches)
    {
        List<MatchRes> matchesRes = new ArrayList<MatchRes>();

        matches.forEach((m) -> matchesRes.add(new MatchRes(m)));

        return matchesRes;
    }
    
}
