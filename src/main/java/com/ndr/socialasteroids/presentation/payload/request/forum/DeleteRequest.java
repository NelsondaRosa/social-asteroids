package com.ndr.socialasteroids.presentation.payload.request.forum;

import lombok.Data;

@Data
public class DeleteRequest
{
    private Long ownerId;
    private Long entityId;
}
