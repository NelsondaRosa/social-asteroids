package com.ndr.socialasteroids.view.controller;

import java.util.List;
import java.util.NoSuchElementException;

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

import com.ndr.socialasteroids.domain.entities.Match;
import com.ndr.socialasteroids.service.MatchService;
import com.ndr.socialasteroids.view.dto.MatchDTO;
import com.ndr.socialasteroids.view.viewobject.MatchVO;
import com.ndr.socialasteroids.view.viewobject.ViewFactory;

@RestController
@RequestMapping("/match")
public class MatchController {
    
    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService){
        this.matchService = matchService;
    }
    
    @PostMapping(path = "/registerMatch")
    @PreAuthorize("#u.getPlayerId() == principal.getUser().getId()")
    public ResponseEntity<?> register(@P("u") @RequestBody MatchDTO matchDTO){
        Match match = matchDTO.toDomainEntity();
        try{
            matchService.registerMatch(matchDTO.getPlayerId(), match);
        } catch (NoSuchElementException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        
        return ResponseEntity.ok().build();  
    }

    @GetMapping(path = "/getMatches/{userId}")
    public ResponseEntity<?> getMatches(@PathVariable Long userId){
        List<Match> matches;
        try{
            matches = matchService.getMatches(userId);
        } catch(Exception ex){
            return ResponseEntity.badRequest().build();
        }

        List<MatchVO> matchesVO = ViewFactory.buildMatchVOList(matches);

        return ResponseEntity.ok().body(matchesVO);
    }
}
