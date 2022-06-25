package com.ndr.socialasteroids.presentation.payload.request.friendship;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AnswerFriendshipRequest
{
    @NotNull private Long userId;
    @NotNull private Long inviterId;
    @NotNull private boolean accepted;

    public AnswerFriendshipRequest(){}
}
