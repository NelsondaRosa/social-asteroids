package com.ndr.socialasteroids.merchant.controller;

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

import com.ndr.socialasteroids.business.service.MatchService;
import com.ndr.socialasteroids.domain.entity.Match;
import com.ndr.socialasteroids.merchant.payload.request.MatchReq;
import com.ndr.socialasteroids.merchant.payload.response.MatchRes;
import com.ndr.socialasteroids.merchant.payload.response.ResponseUtils;

@RestController @RequestMapping("/match")
public class MatchController
{
    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService)
    {
        this.matchService = matchService;
    }

    @PostMapping(path = "/register") @PreAuthorize("#u.getPlayerId() == principal.getUser().getId()")
    public ResponseEntity<?> register(@P("u") @RequestBody MatchReq matchReq)
    {
        Match match = matchReq.toDomainEntity();
        matchService.register(matchReq.getPlayerId(), match);

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/get/{userId}")
    public ResponseEntity<?> getMatches(@PathVariable Long userId)
    {
        List<Match> matches = matchService.getMatches(userId);

        if(matches.size() <= 0)
            return ResponseEntity.noContent().build();

        List<MatchRes> matchesRes = ResponseUtils.createMatchResponseList(matches);
        return ResponseEntity.ok().body(matchesRes);
    }
}
