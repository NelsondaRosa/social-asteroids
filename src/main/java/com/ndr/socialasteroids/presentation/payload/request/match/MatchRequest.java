package com.ndr.socialasteroids.presentation.payload.request.match;

import lombok.Data;

@Data
public class MatchRequest
{
    private Long durationInMilis;
    private Long score;
    private Long ammoSpent;
    private Long destroyedTargets;
    private Long playerId;

    public MatchRequest(){};
    
}
