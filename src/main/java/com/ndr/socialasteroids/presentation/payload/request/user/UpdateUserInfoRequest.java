package com.ndr.socialasteroids.presentation.payload.request.user;

import lombok.Data;

@Data
public class UpdateUserInfoRequest
{
    private Long id;
    private String username;
    private String email;
}
