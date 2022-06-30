package com.ndr.socialasteroids.presentation.payload.request.forum;

import lombok.Data;

@Data
public class DeleteRequest
{
    private Long authorId;
    private Long entityId;
}
