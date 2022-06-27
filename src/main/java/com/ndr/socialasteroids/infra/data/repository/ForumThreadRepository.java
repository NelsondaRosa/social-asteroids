package com.ndr.socialasteroids.infra.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ndr.socialasteroids.domain.entity.ForumThread;
import com.ndr.socialasteroids.domain.entity.User;

@Repository
public interface ForumThreadRepository extends JpaRepository<ForumThread, Long>
{

    Optional<List<ForumThread>> findByUser(User user);
}
