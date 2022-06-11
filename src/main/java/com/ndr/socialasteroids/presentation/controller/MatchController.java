package com.ndr.socialasteroids.presentation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndr.socialasteroids.business.DTOs.MatchDTO;
import com.ndr.socialasteroids.business.service.MatchService;
import com.ndr.socialasteroids.presentation.payload.request.MatchRequest;


@RestController @RequestMapping("/match")
public class MatchController
{
    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService)
    {
        this.matchService = matchService;
    }

    @PostMapping(path = "/register")
    @PreAuthorize("#u.getPlayerId() == principal.getUser().getId()")
    public ResponseEntity<?> register(@P("u") @RequestBody MatchRequest matchReq)
    {
        matchService.register(
            matchReq.getPlayerId(),
            matchReq.getDurationInMilis(),
            matchReq.getScore(),
            matchReq.getAmmoSpent(),
            matchReq.getDestroyedTargets()
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/get/{userId}")
    public ResponseEntity<?> getMatches(@PathVariable Long userId)
    {
        List<MatchDTO> matches = matchService.getMatches(userId);

        if(matches.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok().body(matches);
    }
}
