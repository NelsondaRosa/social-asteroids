package com.ndr.socialasteroids.business.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndr.socialasteroids.business.DTOs.MatchDTO;
import com.ndr.socialasteroids.domain.entity.Match;
import com.ndr.socialasteroids.domain.entity.User;
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

    public MatchDTO register(Long userId, Long duration, Long score, Long ammoSpent, Long destroyedTargets) throws NoSuchElementException, EntityNotFoundException
    {
        User user = userService.getEntityById(userId);
        Match match = new Match(duration, score, ammoSpent, destroyedTargets);

        user.addMatch(match);
        MatchDTO registeredMatch = new MatchDTO(save(match));

        return registeredMatch;
    }

    public List<MatchDTO> getMatches(Long userId) throws EntityNotFoundException
    {
        User player = userService.getEntityById(userId);
        List<MatchDTO> matches = 
                matchRepository.findByPlayer(player).orElseThrow()
                        .stream()
                        .map((match) -> new MatchDTO(match))
                        .collect(Collectors.toList());

        return matches;
    }
}
