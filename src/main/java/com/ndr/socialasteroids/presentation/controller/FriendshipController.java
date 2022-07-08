package com.ndr.socialasteroids.presentation.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.ndr.socialasteroids.business.DTO.FriendshipDTO;
import com.ndr.socialasteroids.business.DTO.UserDTO;
import com.ndr.socialasteroids.business.service.FriendshipService;
import com.ndr.socialasteroids.presentation.payload.request.friendship.AnswerFriendshipRequest;
import com.ndr.socialasteroids.presentation.payload.request.friendship.FriendshipRequest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FriendshipController
{
    private final @NonNull FriendshipService friendshipService;

    @PostMapping(path = "/send-invite")
    @PreAuthorize("#user.getUserId() == principal.getUserSecurityInfo().getId()")
    public ResponseEntity<?> sendInvite(@P("user") @Valid @RequestBody FriendshipRequest friendshipReq)
    {
        friendshipService.sendInvite(friendshipReq.getUserId(), friendshipReq.getFriendId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(path = "/answer-invite")
    @PreAuthorize("#user.getUserId() == principal.getUserSecurityInfo().getId()")
    public ResponseEntity<?> answerInvite(@P("user") @Valid @RequestBody AnswerFriendshipRequest request)
    {       
        UserDTO inviter = 
            friendshipService.answerFriendshipInvite(request.getUserId(), request.getInviterId(), request.isAccepted());
    
        return ResponseEntity.status(HttpStatus.CREATED).body(inviter);
    }

    @DeleteMapping(path = "/unfriend")
    @PreAuthorize("#user.getUserId() == principal.getUserSecurityInfo().getId()")
    public ResponseEntity<?> unfriend(@P("user") @RequestBody FriendshipRequest request)
    {
        friendshipService.unfriend(request.getUserId(), request.getFriendId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(path = "/undo-request")
    @PreAuthorize("#user.getUserId() == principal.getUserSecurityInfo().getId()")
    public ResponseEntity<?> undoRequest( @P("user") @Valid @RequestBody FriendshipRequest request)
    {
        friendshipService.undoRequest(request.getUserId(), request.getFriendId());
        
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path = "/{userId}")
    @PreAuthorize("#userId == principal.getUserSecurityInfo().getId()")
    public ResponseEntity<?> getFriends(@P("userId") @PathVariable Long userId)
    {
        List<FriendshipDTO> friends = friendshipService.getFriends(userId);

        if(friends.size() <= 0)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok().body(friends);
    }

    @GetMapping(path = "/invites/{userId}")
    @PreAuthorize("#userId == principal.getUserSecurityInfo().getId()")
    public ResponseEntity<?> getFriendInvites(@P("userId") @PathVariable Long userId)
    {
        List<FriendshipDTO> friendInvites = friendshipService.getInvites(Long.valueOf(userId));

        if(friendInvites.size() <= 0)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok().body(friendInvites);
    }
}
