package com.ndr.socialasteroids.presentation.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndr.socialasteroids.business.DTO.MatchDTO;
import com.ndr.socialasteroids.business.service.MatchService;
import com.ndr.socialasteroids.presentation.payload.request.match.MatchRequest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RestController @RequestMapping("/match")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MatchController
{
    private final @NonNull MatchService matchService;

    @GetMapping
    public ResponseEntity<?> getPaged(Pageable pageable)
    {
        Page<MatchDTO> matches = matchService.getPaged(pageable);

        return ResponseEntity.ok().body(matches);
    }
    
    @PostMapping(path = "/register")
    @PreAuthorize("#user.getPlayerId() == principal.getUserSecurityInfo().getId()")
    public ResponseEntity<?> register(@P("user") @Valid @RequestBody MatchRequest request)
    {
        matchService.register(
            request.getPlayerId(),
            request.getDurationInMilis(),
            request.getScore(),
            request.getAmmoSpent(),
            request.getDestroyedTargets()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(path = "/get/user/{userId}")
    public ResponseEntity<?> getMatches(@PathVariable String userId, Pageable pageable)
    {
        Page<MatchDTO> matches = matchService.getMatchesByUserId(Long.valueOf(userId), pageable);

        if(matches.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok().body(matches);
    }

    @GetMapping(path = "/get/{matchId}")
    public ResponseEntity<?> getMatch(@PathVariable String matchId)
    {
        MatchDTO match = matchService.getMatch(Long.valueOf(matchId));
        
        return ResponseEntity.ok().body(match);
    }
}
