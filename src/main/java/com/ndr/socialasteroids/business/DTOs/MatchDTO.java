package com.ndr.socialasteroids.business.DTOs;

import java.time.Duration;

import com.ndr.socialasteroids.domain.entity.Match;

import lombok.Data;

@Data
public class MatchDTO
{
    private Duration duration;
    private Long score;
    private Long ammoSpent;
    private Long destroyedTargets;
    
    public MatchDTO(Match match)
    {
        this.duration = match.getDuration();
        this.score = match.getScore();
        this.ammoSpent = match.getAmmoSpent();
        this.destroyedTargets = match.getAmmoSpent();
    }
}
