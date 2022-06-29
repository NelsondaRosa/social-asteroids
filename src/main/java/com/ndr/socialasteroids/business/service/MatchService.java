package com.ndr.socialasteroids.business.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndr.socialasteroids.business.DTOs.MatchDTO;
import com.ndr.socialasteroids.domain.entity.Match;
import com.ndr.socialasteroids.domain.entity.User;
import com.ndr.socialasteroids.infra.data.repository.MatchRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MatchService
{

    private final @NonNull MatchRepository matchRepository;
    private final @NonNull UserService userService;

    public Match save(Match match)
    {
        Match newMatch = matchRepository.saveAndFlush(match);
        return newMatch;
    }

    public MatchDTO register(
            Long userId,
            Long duration,
            Long score,
            Long ammoSpent,
            Long destroyedTargets)
    {
        User user = userService.getEntityById(userId);
        Match match = new Match(duration, score, ammoSpent, destroyedTargets);
        
        match.setPlayer(user);
        MatchDTO registeredMatch = new MatchDTO(save(match));

        return registeredMatch;
    }

    public List<MatchDTO> getMatchesByUserId(Long userId)
    {
        User player = userService.getEntityById(userId);
        List<MatchDTO> matches = 
                matchRepository.findByPlayer(player).orElseThrow()
                        .stream()
                        .map(MatchDTO::new)
                        .collect(Collectors.toList());

        return matches;
    }

    public List<MatchDTO> getAll()
    {
        List<MatchDTO> matches = new ArrayList<MatchDTO>();

        matchRepository.findAllByOrderByScoreDesc().orElseThrow()
                        .stream()
                        .forEach(match -> matches.add(new MatchDTO(match)));
                        
        return matches;
    }

    public MatchDTO getMatch(Long matchId)
    {
        MatchDTO match = new MatchDTO(matchRepository.getById(matchId));
        
        return match;
    }
}
