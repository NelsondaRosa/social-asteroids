package com.ndr.socialasteroids.view.responseobject;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ndr.socialasteroids.domain.entities.Friendship;
import com.ndr.socialasteroids.domain.entities.Match;

@Component
public class ViewFactory {

    public static List<FriendshipRO> buildFriendsVOList(List<Friendship> friends){
        List<FriendshipRO> friendsVO = new ArrayList<FriendshipRO>();

        friends.forEach((f) -> friendsVO.add(new FriendshipRO(f)));

        return friendsVO;
    }

    public static List<FriendshipRO> buildFriendshipVOList(List<Friendship> friendships){
        List<FriendshipRO> friendshipVOList = new ArrayList<FriendshipRO>();

        friendships.forEach((f) -> friendshipVOList.add(new FriendshipRO(f)));

        return friendshipVOList;
    }

    public static List<MatchRO> buildMatchVOList(List<Match> matches){
        List<MatchRO> matchesVO = new ArrayList<MatchRO>();

        matches.forEach((m) -> matchesVO.add(new MatchRO(m)));

        return matchesVO;
    }
    
}
