package com.ndr.socialasteroids.presentation.payload.response;

import lombok.Data;

@Data
public class JwtResponse
{
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long jwtExpires;
    private Long refreshExpires;
    private Long userId;
    private String username;
    
    public JwtResponse(String token, String refreshToken, Long userId, String username)
    {
        this.token = token;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.username = username;
        //TODO
        this.jwtExpires = Long.valueOf(15000);
        //TODO
        this.refreshExpires = Long.valueOf("2628002880");
    }
    
}
