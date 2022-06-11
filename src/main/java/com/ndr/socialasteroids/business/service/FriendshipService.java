package com.ndr.socialasteroids.business.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndr.socialasteroids.domain.entity.User;
import com.ndr.socialasteroids.domain.entity.Friendship;
import com.ndr.socialasteroids.domain.entity.Friendship.Key;
import com.ndr.socialasteroids.infra.data.repository.FriendshipRepository;
import com.ndr.socialasteroids.infra.error.exception.DataInconsistencyException;
import com.ndr.socialasteroids.infra.error.exception.InexistentDataException;

@Service
public class FriendshipService
{
    private final FriendshipRepository friendRepository;
    private final UserService userService;

    @Autowired
    public FriendshipService(FriendshipRepository friendshiRepository, UserService userService)
    {
        this.friendRepository = friendshiRepository;
        this.userService = userService;
    }

    public void sendInvite(Long userId, Long friendId)
            throws NoSuchElementException, EntityNotFoundException, DataInconsistencyException, IllegalArgumentException
    {
        if (relationExists(userId, friendId))
        {
            throw new DataInconsistencyException("Invite already exists");
        }

        User user = userService.getEntityById(userId);
        User friend = userService.getEntityById(friendId);

        // Se a relação já existir do lado oposto,cria a relação do lado atual e
        // ativa o status de amizade
        if (relationExists(friendId, userId))
        {
            Friendship userSide = new Friendship(user, friend, true);
            Friendship friendSide = friendRepository.getById(new Key(friendId, userId));

            friendSide.setAccepted(true);

            friendRepository.saveAndFlush(userSide);
            friendRepository.saveAndFlush(friendSide);
        } else
        {
            Friendship friendship = new Friendship(user, friend, false);

            friendRepository.saveAndFlush(friendship);
        }
    }

    public List<Friendship> getInvites(Long userId) throws NoSuchElementException
    {
        List<Friendship> requests = friendRepository.findInvitesById(userId).orElseThrow();

        return requests;
    }

    public void answerFriendshipInvite(Friendship friendshipRequest, boolean accepted)
            throws DataInconsistencyException, EntityNotFoundException, IllegalArgumentException
    {
        if (relationExists(friendshipRequest.getFriend().getId(), friendshipRequest.getUser().getId()))
            throw new DataInconsistencyException("Friendship already requested by the other part");

        if (accepted)
        {
            friendshipRequest.setAccepted(true);
            Friendship friendship = new Friendship(friendshipRequest.getFriend(), friendshipRequest.getUser(), true);

            friendRepository.saveAndFlush(friendshipRequest);
            friendRepository.saveAndFlush(friendship);
        } else
        {
            friendRepository.delete(friendshipRequest);
        }
    }

    public List<Friendship> getFriends(Long userId) throws NoSuchElementException
    {
        User user = userService.getEntityById(userId);
        List<Friendship> friends = friendRepository.findAllByUser(user).orElseThrow();

        return friends;
    }

    public Friendship getByIds(Long userId, Long friendId) throws EntityNotFoundException
    {
        Friendship friendship = friendRepository.getById(new Key(userId, friendId));
        return friendship;
    }

    public void unrequest(Long userId, Long friendId) throws IllegalArgumentException, DataInconsistencyException
    {
        if (!relationExists(userId, friendId) && !relationExists(friendId, userId))
        {
            throw new DataInconsistencyException("The friendship invte doesn't exists");
        }

        friendRepository.deleteById(new Key(userId, friendId));
        
    }

    public void unfriend(Long userId, Long friendId) throws IllegalArgumentException, InexistentDataException
    {
        if (!relationExists(userId, friendId) && relationExists(friendId, userId))
        {
            throw new InexistentDataException("Friendship doesn't exists");   
        }

        friendRepository.deleteById(new Key(userId, friendId));
        friendRepository.deleteById(new Key(friendId, userId));
    }

    private boolean relationExists(Long userId, Long friendId)
    {
        return friendRepository.existsById(new Key(userId, friendId));
    }

}
