package com.ndr.socialasteroids.infra.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.ndr.socialasteroids.domain.entity.Match;
import com.ndr.socialasteroids.domain.entity.User;

@Repository
public interface MatchRepository extends PagingAndSortingRepository<Match, Long>
{
    Page<Match> findPagedByPlayer(User player, Pageable pageable);

    Page<Match> findPagedByOrderByScoreDesc(Pageable pageable);
    
}
