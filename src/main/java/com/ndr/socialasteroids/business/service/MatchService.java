package com.ndr.socialasteroids.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ndr.socialasteroids.business.DTO.MatchDTO;
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
        Match newMatch = matchRepository.save(match);
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

    public Page<MatchDTO> getMatchesByUserId(Long userId, Pageable pageable)
    {
        User player = userService.getEntityById(userId);

        return matchRepository.findPagedByPlayer(player, pageable)
                        .map(match -> new MatchDTO(match));
    }

    public Page<MatchDTO> getPaged(Pageable pageable)
    {
        return matchRepository.findPagedByOrderByScoreDesc(pageable)
                                .map(match -> new MatchDTO(match));
    }

    public MatchDTO getMatch(Long matchId)
    {
        MatchDTO match = new MatchDTO(matchRepository.findById(matchId).orElseThrow());
        
        return match;
    }
}
