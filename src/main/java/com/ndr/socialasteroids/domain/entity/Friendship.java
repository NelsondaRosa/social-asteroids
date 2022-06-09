package com.ndr.socialasteroids.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ndr.socialasteroids.infra.error.exception.DataInconsistencyException;

import lombok.Data;

@Data
@Entity(name = "friendship")
public class Friendship
{
    @JsonIgnore
    @EmbeddedId
    private Key key = new Key();

    @ManyToOne
    @MapsId("userId")
    private AppUser user;

    @ManyToOne
    @MapsId("friendId")
    private AppUser friend;

    @Column(nullable = false)
    private boolean accepted;

    public Friendship(AppUser user, AppUser friend, boolean accepted)
    {
        this.user = user;
        this.friend = friend;
        this.accepted = accepted;
    }

    public Friendship(){}

    @Data
    @Embeddable
    public static class Key implements Serializable
    {
        private Long userId;
        private Long friendId;

        public Key(Long userId, Long friendId) throws DataInconsistencyException
        {
            if (userId.equals(friendId))
                throw new DataInconsistencyException("You cant invite yourself");
            
            this.userId = userId;
            this.friendId = friendId;
        }

        public Key(){}
    }
    
}
