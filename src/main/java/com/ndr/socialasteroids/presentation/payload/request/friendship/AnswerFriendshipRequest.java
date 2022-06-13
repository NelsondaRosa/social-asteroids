package com.ndr.socialasteroids.presentation.payload.request.friendship;

import lombok.Data;

@Data
public class AnswerFriendshipRequest
{
    private Long userId;
    private Long inviterId;
    private boolean accepted;

    public AnswerFriendshipRequest(){}
}
