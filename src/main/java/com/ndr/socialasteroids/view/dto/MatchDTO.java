package com.ndr.socialasteroids.view.dto;

import com.ndr.socialasteroids.domain.entities.Match;

import lombok.Data;

@Data
public class MatchDTO {

    private Long durationInMilis;

    private Long score;

    private Long ammoSpent;

    private Long destroyedTargets;

    private Long playerId;

    public Match toDomainEntity(){
        Match match = new Match(this.getDurationInMilis(), this.getScore(),
                                    this.getAmmoSpent(), this.getDestroyedTargets());
        match.setTimeNow();

        return match;
    }

    public MatchDTO(){};
    
}
