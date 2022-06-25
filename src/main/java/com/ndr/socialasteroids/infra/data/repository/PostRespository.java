package com.ndr.socialasteroids.infra.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ndr.socialasteroids.domain.entity.Post;

@Repository
public interface PostRespository extends JpaRepository<Post, Long> {
    
}
