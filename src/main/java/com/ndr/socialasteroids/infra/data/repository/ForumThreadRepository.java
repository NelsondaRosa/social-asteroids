package com.ndr.socialasteroids.infra.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.ndr.socialasteroids.domain.entity.ForumThread;
import com.ndr.socialasteroids.domain.entity.User;

@Repository
public interface ForumThreadRepository extends PagingAndSortingRepository<ForumThread, Long>
{
    Page<ForumThread> findByOwner(User owner, Pageable pageable);

    Page<ForumThread> findAll(Pageable pageable);
}
