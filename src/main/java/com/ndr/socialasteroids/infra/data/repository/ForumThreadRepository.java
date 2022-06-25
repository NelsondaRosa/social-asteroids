package com.ndr.socialasteroids.infra.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ndr.socialasteroids.domain.entity.ForumThread;

@Repository
public interface ForumThreadRepository extends JpaRepository<ForumThread, Long>{
    
}
