package com.ndr.socialasteroids.view.controller;

import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityNotFoundException;

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

import com.ndr.socialasteroids.domain.entities.Friendship;
import com.ndr.socialasteroids.domain.exceptions.DataInconsistencyException;
import com.ndr.socialasteroids.service.FriendshipService;
import com.ndr.socialasteroids.view.dto.BlindFriendshipDTO;
import com.ndr.socialasteroids.view.dto.FriendAnswerDTO;
import com.ndr.socialasteroids.view.dto.enums.UnfriendMode;
import com.ndr.socialasteroids.view.responseobject.FriendshipRO;
import com.ndr.socialasteroids.view.responseobject.ViewFactory;
@RestController
@RequestMapping("/friend")
public class FriendshipController {
    
    private final FriendshipService friendshipService;

    @Autowired
    public FriendshipController(FriendshipService friendshipService){
        this.friendshipService = friendshipService;
    }


    @PostMapping(path = "/private/sendRequest")
    @PreAuthorize("#u.getUserId() == principal.getUser().getId()")
    public ResponseEntity<?> sendRequest(@P("u") @RequestBody BlindFriendshipDTO friendshipDTO){
        try{
            friendshipService.sendRequest(friendshipDTO.getUserId(), friendshipDTO.getFriendId());
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (DataInconsistencyException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/answerRequest")
    @PreAuthorize("#u.getRequestedId() == principal.getUser().getId()")
    public ResponseEntity<?> answerRequest(@P("u") @RequestBody FriendAnswerDTO friendAnswerDTO){
        try{
            Friendship friendshipRequest = 
                friendshipService.getByIds(friendAnswerDTO.getRequesterId(), friendAnswerDTO.getRequestedId());
            friendshipService.answerFriendshipRequest(friendshipRequest, friendAnswerDTO.isAccepted());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.badRequest().body("Requisição não existe");
        } catch (DataInconsistencyException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/private/unfriend")
    @PreAuthorize("#u.getUserId() == principal.getUser().getId()")
    public ResponseEntity<?> unfriend(@P("u") @RequestBody BlindFriendshipDTO friendshipDTO){

        try{
            friendshipService.unfriend(friendshipDTO.getUserId(), friendshipDTO.getFriendId(), UnfriendMode.UNFRIEND);
        } catch (Exception ex){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/private/unrequest")
    @PreAuthorize("#u.getUserId() == principal.getUser().getId()")
    public ResponseEntity<?> unrequest( @P("u") @RequestBody BlindFriendshipDTO friendshipDTO){

        try{
            friendshipService.unfriend(friendshipDTO.getUserId(), friendshipDTO.getFriendId(), UnfriendMode.UNREQUEST);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/private/getFriends/{userId}")
    @PreAuthorize("#u == principal.getUser().getId()")
    public ResponseEntity<?> getFriends(@P("u") @PathVariable Long userId){

        List<Friendship> friends;
        try{
            friends = friendshipService.getFriends(userId);
        } catch (NoSuchElementException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        List<FriendshipRO> friendsVO = ViewFactory.buildFriendsVOList(friends);

        return ResponseEntity.ok().body(friendsVO);
    }

    @GetMapping(path = "/getRequests/{userId}")
    @PreAuthorize("#u == principal.getUser().getId()")
    public ResponseEntity<?> getFriendRequests(@P("u") @PathVariable Long userId){

        List<Friendship> friendRequests;
        try{
            friendRequests = friendshipService.getRequests(userId);
        } catch (NoSuchElementException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        List<FriendshipRO> friendshipVO = ViewFactory.buildFriendshipVOList(friendRequests);
        
        return ResponseEntity.ok().body(friendshipVO);
    }
}
