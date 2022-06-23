package com.ndr.socialasteroids.infra.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ndr.socialasteroids.domain.entity.Match;
import com.ndr.socialasteroids.domain.entity.User;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>
{
    Optional<List<Match>> findByPlayer(User player);
    
}
