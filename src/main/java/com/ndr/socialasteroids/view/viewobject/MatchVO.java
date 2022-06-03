package com.ndr.socialasteroids.view.viewobject;

import java.time.Duration;

import com.ndr.socialasteroids.domain.entities.Match;

import lombok.Data;

@Data
public class MatchVO {
    private Duration duration;
    private Long score;
    private Long ammoSpent;
    private Long destroyedTargets;

    public MatchVO(Match match){
        this.duration = match.getDuration();
        this.score = match.getScore();
        this.ammoSpent = match.getAmmoSpent();
        this.destroyedTargets = match.getDestroyedTargets();
    }

    public MatchVO(){}
}
