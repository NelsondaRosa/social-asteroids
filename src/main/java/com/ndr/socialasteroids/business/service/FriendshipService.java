package com.ndr.socialasteroids.business.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndr.socialasteroids.business.DTO.FriendshipDTO;
import com.ndr.socialasteroids.business.DTO.UserDTO;
import com.ndr.socialasteroids.domain.entity.Friendship;
import com.ndr.socialasteroids.domain.entity.Friendship.Key;
import com.ndr.socialasteroids.domain.entity.User;
import com.ndr.socialasteroids.infra.data.repository.FriendshipRepository;
import com.ndr.socialasteroids.infra.error.exception.DataInconsistencyException;
import com.ndr.socialasteroids.infra.error.exception.InexistentDataException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
//TODO implement paged
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FriendshipService
{
    private final @NonNull FriendshipRepository friendRepository;
    private final @NonNull UserService userService;

    public void sendInvite(Long userId, Long friendId)
    {
        if (relationExists(userId, friendId))
        {
            throw new DataInconsistencyException("Invite already exists");
        }

        User user = userService.getEntityById(userId);
        User friend = userService.getEntityById(friendId);

        //If relation already exists in opposite side, create the relation in actual side and
        //activate the friend status
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

    public List<FriendshipDTO> getInvites(Long userId)
    {
       var inviteList = friendRepository.findInvitesById(userId).orElseThrow();
    
       return friendshipCollectionToDTOList(inviteList);
    }

    public UserDTO answerFriendshipInvite(Long userId, Long inviterId, Boolean accepted)
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

    public List<FriendshipDTO> getFriends(Long userId)
    {
        var friendsList = friendRepository.findAllByUser(userId).orElseThrow();
                
        return friendshipCollectionToDTOList(friendsList);
    }

    public void undoRequest(Long userId, Long friendId)
    {
        if (!relationExists(friendId, userId))
            throw new DataInconsistencyException("The invite doesn't exist");

        Friendship inverseFriendship = getByIds(friendId, userId);
        if (inverseFriendship.isAccepted())
            throw new DataInconsistencyException("Cant unrequest a friendship that is accepted");

        friendRepository.deleteById(new Key(friendId, userId));
    }

    public void unfriend(Long userId, Long friendId)
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

    private Friendship getByIds(Long userId, Long friendId)
    {
        Friendship friendship = friendRepository.getById(new Key(userId, friendId));
        return friendship;
    }

    private List<FriendshipDTO> friendshipCollectionToDTOList(Collection<Friendship> collection)
    {
        return collection
                    .stream()
                    .map(FriendshipDTO::new)
                    .collect(Collectors.toList());
    }
}
