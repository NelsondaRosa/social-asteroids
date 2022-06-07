package com.ndr.socialasteroids.dealer.payload.response;

import java.time.Duration;

import com.ndr.socialasteroids.domain.entity.Match;

import lombok.Data;

@Data
public class MatchRes {
    private Duration duration;
    private Long score;
    private Long ammoSpent;
    private Long destroyedTargets;

    public MatchRes(Match match){
        this.duration = match.getDuration();
        this.score = match.getScore();
        this.ammoSpent = match.getAmmoSpent();
        this.destroyedTargets = match.getDestroyedTargets();
    }

    public MatchRes(){}
}
