package com.ndr.socialasteroids.infra.data.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ndr.socialasteroids.domain.entities.AppUser;
import com.ndr.socialasteroids.domain.entities.Match;

@Repository
public interface IMatchRepository extends JpaRepository<Match, Long>{

    List<Match> findByPlayer(AppUser player);
    
}
