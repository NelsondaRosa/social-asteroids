package com.ndr.socialasteroids.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndr.socialasteroids.domain.entity.AppUser;
import com.ndr.socialasteroids.domain.entity.Match;
import com.ndr.socialasteroids.infra.data.repository.MatchRepository;

@Service
public class MatchService
{

    private final MatchRepository matchRepository;
    private final UserService userService;

    @Autowired
    public MatchService(MatchRepository matchRepository, UserService userService)
    {
        this.matchRepository = matchRepository;
        this.userService = userService;
    }

    public Match save(Match match)
    {
        Match newMatch = matchRepository.saveAndFlush(match);
        return newMatch;
    }

    public Match register(Long userId, Match match) throws NoSuchElementException, EntityNotFoundException
    {
        AppUser user = userService.getById(userId);
        user.addMatch(match);

        Match registeredMatch = save(match);

        return registeredMatch;
    }

    public List<Match> getMatches(Long userId) throws EntityNotFoundException
    {
        AppUser player = userService.getById(userId);
        return matchRepository.findByPlayer(player);
    }
}
