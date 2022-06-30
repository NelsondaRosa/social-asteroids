package com.ndr.socialasteroids.presentation.payload.request.forum;

import lombok.Data;

@Data
public class CreateThreadRequest
{
    private Long authorId;
    private String title;
}
