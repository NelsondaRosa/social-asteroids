package com.ndr.socialasteroids.view.dto;

import lombok.Data;

@Data
public class FriendAnswerDTO {
    
    private Long requestedId;
    private Long requesterId;
    private boolean accepted;

    public FriendAnswerDTO(){}
}
