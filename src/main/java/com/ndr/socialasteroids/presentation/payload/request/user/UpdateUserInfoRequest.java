package com.ndr.socialasteroids.presentation.payload.request.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateUserInfoRequest
{
    @NotNull private Long id;
    @NotBlank private String username;
    @NotBlank private String email;
}
