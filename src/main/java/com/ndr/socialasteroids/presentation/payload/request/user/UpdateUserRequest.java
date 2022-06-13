package com.ndr.socialasteroids.presentation.payload.request.user;

import lombok.Data;

@Data
public class UpdateUserRequest
{
    private Long id;
    private String username;
    private String email;
}
