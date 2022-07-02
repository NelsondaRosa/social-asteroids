package com.ndr.socialasteroids.presentation.payload.request.forum;

import lombok.Data;

@Data //TODO validation
public class EditPostRequest
{
    private Long postId;
    private Long authorId;
    private String content;
}
