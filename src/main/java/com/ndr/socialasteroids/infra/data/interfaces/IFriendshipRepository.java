package com.ndr.socialasteroids.infra.data.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ndr.socialasteroids.domain.entities.AppUser;
import com.ndr.socialasteroids.domain.entities.Friendship;

@Repository
public interface IFriendshipRepository extends JpaRepository<Friendship, Friendship.Key>{

    List<Friendship> findAllByUser(AppUser user);
    
    @Query(value = "SELECT * FROM friendship WHERE NOT accepted AND friend_id = :user_id", nativeQuery = true)
    List<Friendship> findRequestsById(@Param("user_id") Long userId);
}
