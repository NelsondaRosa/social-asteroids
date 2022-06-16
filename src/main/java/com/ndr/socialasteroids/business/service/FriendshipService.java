package com.ndr.socialasteroids.business.service;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndr.socialasteroids.business.DTOs.FriendshipDTO;
import com.ndr.socialasteroids.business.DTOs.UserDTO;
import com.ndr.socialasteroids.domain.entity.Friendship;
import com.ndr.socialasteroids.domain.entity.Friendship.Key;
import com.ndr.socialasteroids.domain.entity.User;
import com.ndr.socialasteroids.infra.data.repository.FriendshipRepository;
import com.ndr.socialasteroids.infra.error.exception.DataInconsistencyException;
import com.ndr.socialasteroids.infra.error.exception.InexistentDataException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FriendshipService
{
    private final @NonNull FriendshipRepository friendRepository;
    private final @NonNull UserService userService;

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

    public List<FriendshipDTO> getInvites(Long userId) throws NoSuchElementException
    {
       var inviteList = friendRepository.findInvitesById(userId).orElseThrow();
    
       return entityCollectionToDTOList(inviteList);
    }

    public UserDTO answerFriendshipInvite(Long userId, Long inviterId, Boolean accepted)
            throws DataInconsistencyException, EntityNotFoundException, IllegalArgumentException
    {
        if (relationExists(userId, inviterId))
            throw new DataInconsistencyException("Invite or friendship already exist");

        User user = userService.getEntityById(userId);
        User friend = userService.getEntityById(inviterId);
        Friendship invite = getByIds(inviterId, userId);

        if (accepted)
        {
            Friendship answer = new Friendship(user, friend, true);
            invite.setAccepted(true);
            
            friendRepository.saveAndFlush(answer);
            friendRepository.saveAndFlush(invite);
        } else
        {
            invite = getByIds(inviterId, userId);
            friendRepository.delete(invite);
        }

        return new UserDTO(friend);
    }

    public List<FriendshipDTO> getFriends(Long userId) throws NoSuchElementException
    {
        User user = userService.getEntityById(userId);
        var friendsList = friendRepository.findAllByUser(user).orElseThrow();
                
        return entityCollectionToDTOList(friendsList);
    }

    public void unrequest(Long userId, Long friendId) throws IllegalArgumentException, DataInconsistencyException
    {
        if (!relationExists(friendId, userId))
            throw new DataInconsistencyException("The invite doesn't exist");

        Friendship inverseFriendship = getByIds(friendId, userId);
        if (inverseFriendship.isAccepted())
            throw new DataInconsistencyException("Cant unrequest a friendship that is accepted");

        friendRepository.deleteById(new Key(friendId, userId));
    }

    public void unfriend(Long userId, Long friendId) throws IllegalArgumentException, InexistentDataException
    {
        if (!relationExists(userId, friendId))
            throw new InexistentDataException("Friendship doesn't exists");   

        friendRepository.deleteById(new Key(userId, friendId));
        friendRepository.deleteById(new Key(friendId, userId));
    }

    private boolean relationExists(Long userId, Long friendId)
    {
        return friendRepository.existsById(new Key(userId, friendId));
    }

    private Friendship getByIds(Long userId, Long friendId) throws EntityNotFoundException
    {
        Friendship friendship = friendRepository.getById(new Key(userId, friendId));
        return friendship;
    }

    private List<FriendshipDTO> entityCollectionToDTOList(Collection<Friendship> collection)
    {
        return collection
                    .stream()
                    .map(FriendshipDTO::new)
                    .collect(Collectors.toList());
    }

}
