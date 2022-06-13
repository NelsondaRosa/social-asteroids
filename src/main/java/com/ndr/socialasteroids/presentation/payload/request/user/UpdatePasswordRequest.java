package com.ndr.socialasteroids.presentation.payload.request.user;

import lombok.Data;

@Data
public class UpdatePasswordRequest
{
    private Long id;
    private String actualPassword;
    private String newPassword;
}
