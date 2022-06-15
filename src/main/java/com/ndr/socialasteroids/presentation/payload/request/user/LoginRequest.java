package com.ndr.socialasteroids.presentation.payload.request.user;

import lombok.Data;

@Data
public class LoginRequest
{
    private String username;
    private String password;
    
}
