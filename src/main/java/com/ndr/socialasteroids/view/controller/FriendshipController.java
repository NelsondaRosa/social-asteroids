package com.ndr.socialasteroids.view.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndr.socialasteroids.domain.entities.AppUserDetails;
import com.ndr.socialasteroids.domain.entities.Friendship;
import com.ndr.socialasteroids.domain.exceptions.DataInconsistencyException;
import com.ndr.socialasteroids.service.FriendshipService;
import com.ndr.socialasteroids.view.dto.AppUserDTO;
import com.ndr.socialasteroids.view.dto.BlindFriendshipDTO;
import com.ndr.socialasteroids.view.dto.FriendAnswerDTO;
import com.ndr.socialasteroids.view.dto.enums.UnfriendMode;
import com.ndr.socialasteroids.view.viewobject.FriendshipVO;
import com.ndr.socialasteroids.view.viewobject.ViewFactory;
@RestController
@RequestMapping("/friend")
public class FriendshipController {
    
    private final FriendshipService friendshipService;

    @Autowired
    public FriendshipController(FriendshipService friendshipService){
        this.friendshipService = friendshipService;
    }


    @PostMapping(path = "/sendRequest")
    public ResponseEntity<?> sendRequest(@RequestBody BlindFriendshipDTO friendshipDTO, @AuthenticationPrincipal AppUserDetails principal){
        if(!checkCurrentUser(friendshipDTO.getUserId(), principal.getUser())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

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
    public ResponseEntity<?> answerRequest(@RequestBody FriendAnswerDTO friendAnswerDTO, @AuthenticationPrincipal AppUserDetails principal){
        if(!checkCurrentUser(friendAnswerDTO.getRequestedId(), principal.getUser())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        
        try{
            Friendship friendshipRequest = 
                friendshipService.getByIds(friendAnswerDTO.getRequesterId(), friendAnswerDTO.getRequestedId());
            friendshipService.answerFriendshipRequest(friendshipRequest, friendAnswerDTO.isAccepted());
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (DataInconsistencyException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/unfriend")
    public ResponseEntity<?> unfriend(@RequestBody BlindFriendshipDTO friendshipDTO,
        @AuthenticationPrincipal AppUserDetails principal){

        if(!checkCurrentUser(friendshipDTO.getUserId(), principal.getUser())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try{
            friendshipService.unfriend(friendshipDTO.getUserId(), friendshipDTO.getFriendId(), UnfriendMode.UNFRIEND);
        } catch (Exception ex){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/unrequest")
    public ResponseEntity<?> unrequest(@RequestBody BlindFriendshipDTO friendshipDTO,
        @AuthenticationPrincipal AppUserDetails principal){
        
        if(!checkCurrentUser(friendshipDTO.getUserId(), principal.getUser())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try{
            friendshipService.unfriend(friendshipDTO.getUserId(), friendshipDTO.getFriendId(), UnfriendMode.UNREQUEST);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/getFriends/{userId}")
    public ResponseEntity<?> getFriends(@PathVariable Long userId, @AuthenticationPrincipal AppUserDetails principal){

        if(!checkCurrentUser(userId, principal.getUser())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Friendship> friends;
        try{
            friends = friendshipService.getFriends(userId);
        } catch (NoSuchElementException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        List<FriendshipVO> friendsVO = ViewFactory.buildFriendsVOList(friends);

        return ResponseEntity.ok().body(friendsVO);
    }

    @GetMapping(path = "/getRequests/{userId}")
    public ResponseEntity<?> getFriendRequests(@PathVariable Long userId, @AuthenticationPrincipal AppUserDetails principal){

        if(!checkCurrentUser(userId, principal.getUser())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Friendship> friendRequests;
        try{
            friendRequests = friendshipService.getRequests(userId);
        } catch (NoSuchElementException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        List<FriendshipVO> friendshipVO = ViewFactory.buildFriendshipVOList(friendRequests);

        return ResponseEntity.ok().body(friendshipVO);
    }

    //TODO:: Inserir interceptor ou transferir para Utils, ou ainda anotação spring com P()
    private boolean checkCurrentUser(Long userId, AppUserDTO principal){
        if(userId == principal.getId()){
            return true;
        }

        return false;
    }

}
