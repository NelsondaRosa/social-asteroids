package com.ndr.socialasteroids.infra.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.ndr.socialasteroids.domain.entity.ForumThread;
import com.ndr.socialasteroids.domain.entity.Post;
import com.ndr.socialasteroids.domain.entity.User;

@Repository
public interface PostRespository extends PagingAndSortingRepository<Post, Long>
{
    Page<Post> findByThread(ForumThread thread, Pageable pageable);

    Page<Post> findByAuthor(User author, Pageable pageable);
}
