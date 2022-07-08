package com.ndr.socialasteroids.infra.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ndr.socialasteroids.domain.entity.Friendship;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Friendship.Key>
{

    @Query(value = "SELECT * FROM friendship WHERE accepted AND user_id = :user_id", nativeQuery = true)
    Optional<List<Friendship>> findAllByUser(@Param("user_id") Long userId);
    
    @Query(value = "SELECT * FROM friendship WHERE NOT accepted AND friend_id = :user_id", nativeQuery = true)
    Optional<List<Friendship>> findInvitesById(@Param("user_id") Long userId);
}
