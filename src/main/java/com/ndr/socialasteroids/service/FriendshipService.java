package com.ndr.socialasteroids.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndr.socialasteroids.domain.entities.AppUser;
import com.ndr.socialasteroids.domain.entities.Friendship;
import com.ndr.socialasteroids.domain.entities.Friendship.Key;
import com.ndr.socialasteroids.domain.exceptions.DataInconsistencyException;
import com.ndr.socialasteroids.infra.data.interfaces.IFriendshipRepository;
import com.ndr.socialasteroids.view.dto.enums.UnfriendMode;

@Service
public class FriendshipService {
    private final IFriendshipRepository friendRepository;
    private final UserService userService;

    @Autowired
    public FriendshipService(IFriendshipRepository friendshiRepository, UserService userService){
        this.friendRepository = friendshiRepository;
        this.userService = userService;
    }

    public void sendRequest(Long requesterId, Long requestedId) throws NoSuchElementException, EntityNotFoundException, DataInconsistencyException{
        if(relationExists(requesterId, requestedId)){
            throw new DataInconsistencyException();
        
        //Se a relação já existir do lado oposto,cria a relação do lado atual e ativa o status de amizade
        } else if(relationExists(requestedId, requesterId)){
            AppUser requester = userService.getById(requesterId);
            AppUser requested = userService.getById(requestedId);
            Friendship friendshipRequester = new Friendship(requester,requested, true);
            Friendship friendshipRequested = friendRepository.getById(new Key(requestedId, requesterId));
            friendshipRequested.setAccepted(true);

            friendRepository.saveAndFlush(friendshipRequester);
            friendRepository.saveAndFlush(friendshipRequested);
            
            return;
        }
        
        AppUser requester = userService.getById(requesterId);
        AppUser requested = userService.getById(requestedId);

        Friendship friendship = new Friendship(requester,requested, false);

        friendRepository.saveAndFlush(friendship);
    }

    public List<Friendship> getRequests(Long userId){
        List<Friendship> requests = friendRepository.findRequestsById(userId);

        return requests;

    }

    public void answerFriendshipRequest(Friendship friendshipRequest, boolean accepted) throws DataInconsistencyException, EntityNotFoundException{
        //Evita deleção de dados parciais
        if(relationExists(friendshipRequest.getFriend().getId(), friendshipRequest.getUser().getId()))
            throw new DataInconsistencyException();

        if(accepted){
            friendshipRequest.setAccepted(true);
            Friendship friendship = new Friendship(friendshipRequest.getFriend(), friendshipRequest.getUser(), true);

            friendRepository.saveAndFlush(friendshipRequest);
            friendRepository.saveAndFlush(friendship);
        } else {
            friendRepository.delete(friendshipRequest);
        }
    }

    public List<Friendship> getFriends(Long userId) throws EntityNotFoundException{
        AppUser user = userService.getById(userId);
        List<Friendship> friends = friendRepository.findAllByUser(user);

        return friends;
    }

    public Friendship getByIds(Long userId, Long friendId) throws EntityNotFoundException{
        Friendship friendship = friendRepository.getById(new Key(userId, friendId));
        return friendship;
    }

    public void unfriend(Long userId, Long friendId, UnfriendMode unfriendMode) throws EntityNotFoundException{
        if(unfriendMode == UnfriendMode.UNFRIEND && friendRepository.existsById(new Key(friendId, userId))){
            friendRepository.deleteById(new Key(userId, friendId));
            friendRepository.deleteById(new Key(friendId, userId));
        } else if(unfriendMode == UnfriendMode.UNREQUEST){
            friendRepository.deleteById(new Key(userId, friendId));
        }
    }

    //Verifica se relação já existe no BD para não criar inconsistência de dados
    private boolean relationExists(Long userId, Long friendId){
        if(friendRepository.existsById(new Key(userId, friendId))){
            return true;
        }

        return false;
    }  

}
