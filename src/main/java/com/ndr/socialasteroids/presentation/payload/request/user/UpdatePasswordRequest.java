package com.ndr.socialasteroids.presentation.payload.request.user;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdatePasswordRequest
{
    @NotNull private Long id;
    @NotNull private String actualPassword;
    @NotNull private String newPassword;
}
