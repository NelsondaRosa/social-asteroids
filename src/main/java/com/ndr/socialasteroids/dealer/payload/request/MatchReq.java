package com.ndr.socialasteroids.dealer.payload.request;

import com.ndr.socialasteroids.domain.entity.Match;

import lombok.Data;

@Data
public class MatchReq {

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

    public MatchReq(){};
    
}
