package com.ndr.socialasteroids.presentation.payload.request.match;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MatchRequest
{
    @NotNull private Long durationInMilis;
    @NotNull private Long score;
    @NotNull private Long ammoSpent;
    @NotNull private Long destroyedTargets;
    @NotNull private Long playerId;

    public MatchRequest(){};
    
}
