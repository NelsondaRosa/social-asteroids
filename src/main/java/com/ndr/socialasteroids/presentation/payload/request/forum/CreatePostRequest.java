package com.ndr.socialasteroids.presentation.payload.request.forum;

import lombok.Data;

@Data
public class CreatePostRequest
{
    private Long ownerId;
    private Long threadId;
    private String content;
    
}
