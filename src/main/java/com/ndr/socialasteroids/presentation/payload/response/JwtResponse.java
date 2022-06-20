package com.ndr.socialasteroids.presentation.payload.response;

import lombok.Data;

@Data
public class JwtResponse
{
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long userId;
    private String username;
    
    public JwtResponse(String token, String refreshToken, Long userId, String username)
    {
        this.token = token;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.username = username;
    }
    
}
