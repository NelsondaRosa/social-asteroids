package com.ndr.socialasteroids.presentation.payload.request.forum;

import lombok.Data;

@Data //TODO validation
public class CreateThreadRequest
{
    private Long authorId;
    private String title;
}
