package com.ndr.socialasteroids.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ndr.socialasteroids.business.DTOs.UserDTO;
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
    private final @NonNull JwtUtils jwtUtils;

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

    public ResponseCookie createRefreshToken()
    {
        //refreshTokenService.createRefreshToken(user.getId());

    }

    public ResponseCookie createJwtCookie()
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        return jwtCookie;
    }

    public ResponseCookie generateJwtCookieFromRefreshToken(String refreshTokenString)
    {
        System.out.println(refreshTokenString);
        RefreshToken refreshToken = refreshTokenService.findRefreshToken(refreshTokenString);
        var principal = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        //Operation can't be done by anonymous user
        if (!(principal instanceof UserDetails))
            throw new AccessDeniedException("Access denied");

        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        
        if (refreshToken.getUser().getId() != userDetails.getUserSecurityInfo().getId())
            throw new AccessDeniedException("Access denied");
            
        refreshTokenService.verifyExpiration(refreshToken);

        ResponseCookie newTokenCookie = jwtUtils.generateJwtCookie(userDetails);
        return newTokenCookie;
    }

    
}
