package com.ndr.socialasteroids.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ndr.socialasteroids.business.DTOs.UserDTO;
import com.ndr.socialasteroids.infra.error.exception.RefreshTokenException;
import com.ndr.socialasteroids.security.UserDetailsImpl;
import com.ndr.socialasteroids.security.JWT.JwtUtils;
import com.ndr.socialasteroids.security.JWT.RefreshToken;
import com.ndr.socialasteroids.security.JWT.RefreshTokenService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

//TODO:: Onde ta verificando se o token atual expirou?
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthService
{
    private final @NonNull UserService userService;
    private final @NonNull AuthenticationManager authManager;
    private final @NonNull RefreshTokenService refreshTokenService;

    @Value("${sa.jwt.refresh-expiration-ms}")
    private long refreshTokenDurationMs;
    
    //Called the when the user log in, the filter will not receive this data, so its set here for this request
    //Also, the filter doesnt authenticate, as the JwtToken secret is owned by the server, it doesnt need user authentication in every request
    public UserDTO authenticateUser(String username, String password)
    {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        UserDTO user = new UserDTO(userDetails.getUserSecurityInfo());
        return user;
    }

    public ResponseCookie createJwtCookie()
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseCookie jwtCookie = JwtUtils.generateJwtCookie(userDetails);

        return jwtCookie;
    }

    public ResponseCookie generateJwtCookieFromRefreshToken(String refreshToken)
    {
        RefreshToken refreshTokenEntity = refreshTokenService.findRefreshToken(refreshToken);
        
        if (refreshTokenEntity.isExpired())
        {
            throw new RefreshTokenException("Refresh token is expired. Please, login again");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseCookie newTokenCookie = JwtUtils.generateJwtCookie(userDetails);
        return newTokenCookie;

    }

    
}
