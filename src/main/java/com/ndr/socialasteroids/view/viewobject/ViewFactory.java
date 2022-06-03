package com.ndr.socialasteroids.view.viewobject;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ndr.socialasteroids.domain.entities.Friendship;
import com.ndr.socialasteroids.domain.entities.Match;

@Component
public class ViewFactory {

    public static List<FriendshipVO> buildFriendsVOList(List<Friendship> friends){
        List<FriendshipVO> friendsVO = new ArrayList<FriendshipVO>();

        friends.forEach((f) -> friendsVO.add(new FriendshipVO(f)));

        return friendsVO;
    }

    public static List<FriendshipVO> buildFriendshipVOList(List<Friendship> friendships){
        List<FriendshipVO> friendshipVOList = new ArrayList<FriendshipVO>();

        friendships.forEach((f) -> friendshipVOList.add(new FriendshipVO(f)));

        return friendshipVOList;
    }

    public static List<MatchVO> buildMatchVOList(List<Match> matches){
        List<MatchVO> matchesVO = new ArrayList<MatchVO>();

        matches.forEach((m) -> matchesVO.add(new MatchVO(m)));

        return matchesVO;
    }
    
}
