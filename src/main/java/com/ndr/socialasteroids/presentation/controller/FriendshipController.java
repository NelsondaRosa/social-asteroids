package com.ndr.socialasteroids.presentation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndr.socialasteroids.business.service.FriendshipService;
import com.ndr.socialasteroids.domain.entity.Friendship;
import com.ndr.socialasteroids.presentation.payload.request.BlindFriendshipReq;
import com.ndr.socialasteroids.presentation.payload.request.FriendInviteAnswerReq;
import com.ndr.socialasteroids.presentation.payload.response.FriendshipRes;
import com.ndr.socialasteroids.presentation.payload.response.ResponseUtils;
@RestController
@RequestMapping("/friend")
public class FriendshipController
{
    private final FriendshipService friendshipService;

    @Autowired
    public FriendshipController(FriendshipService friendshipService)
    {
        this.friendshipService = friendshipService;
    }

    @PostMapping(path = "/send-invite")
    @PreAuthorize("#u.getUserId() == principal.getUser().getId()")
    public ResponseEntity<?> sendInvite(@P("u") @RequestBody BlindFriendshipReq friendshipReq)
    {
        friendshipService.sendInvite(friendshipReq.getUserId(), friendshipReq.getFriendId());
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/answer-invite")
    @PreAuthorize("#u.getRequestedId() == principal.getUser().getId()")
    public ResponseEntity<?> answerInvite(@P("u") @RequestBody FriendInviteAnswerReq friendAnswerReq)
    {
        Friendship friendshipRequest = 
            friendshipService.getByIds(friendAnswerReq.getRequesterId(), friendAnswerReq.getRequestedId());
            
        friendshipService.answerFriendshipInvite(friendshipRequest, friendAnswerReq.isAccepted());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/unfriend")
    @PreAuthorize("#u.getUserId() == principal.getUser().getId()")
    public ResponseEntity<?> unfriend(@P("u") @RequestBody BlindFriendshipReq friendshipReq)
    {
        //+++friendshipService.unfriend(friendshipReq.getUserId(), friendshipReq.getFriendId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/unrequest")
    @PreAuthorize("#u.getUserId() == principal.getUser().getId()")
    public ResponseEntity<?> unrequest( @P("u") @RequestBody BlindFriendshipReq friendshipReq)
    {
        friendshipService.unrequest(friendshipReq.getUserId(), friendshipReq.getFriendId());
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/get/{userId}")
    @PreAuthorize("#u == principal.getUser().getId()")
    public ResponseEntity<?> getFriends(@P("u") @PathVariable Long userId)
    {
        List<Friendship> friends = friendshipService.getFriends(userId);

        if(friends.size() <= 0)
            return ResponseEntity.noContent().build();

        List<FriendshipRes> friendsResponse = ResponseUtils.createFriendshipResponseList(friends);
        return ResponseEntity.ok().body(friendsResponse);
    }

    @GetMapping(path = "/invites/{userId}")
    @PreAuthorize("#u == principal.getUser().getId()")
    public ResponseEntity<?> getFriendInvites(@P("u") @PathVariable Long userId)
    {
        List<Friendship> friendInvites = friendshipService.getInvites(userId);

        if(friendInvites.size() <= 0)
            return ResponseEntity.noContent().build();

        List<FriendshipRes> friendInviteRes = ResponseUtils.createFriendshipResponseList(friendInvites);
        return ResponseEntity.ok().body(friendInviteRes);
    }
}
