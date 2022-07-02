package com.ndr.socialasteroids.presentation.payload.request.forum;

import lombok.Data;

@Data //TODO validation
public class DeleteRequest
{
    private Long authorId;
    private Long entityId;
}
