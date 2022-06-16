package com.ndr.socialasteroids.presentation.payload.response;

import java.util.List;

import lombok.Data;

@Data
public class JwtResponse
{
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String username;
    private List<String> roles;
    
    public JwtResponse(String accessToken, String refreshToken, Long id, String username, List<String> roles)
    {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
    
}
