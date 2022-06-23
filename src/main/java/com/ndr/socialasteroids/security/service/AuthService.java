package com.ndr.socialasteroids.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ndr.socialasteroids.business.DTOs.UserDTO;
import com.ndr.socialasteroids.business.service.UserService;
import com.ndr.socialasteroids.security.entities.RefreshToken;
import com.ndr.socialasteroids.security.entities.UserDetailsImpl;
import com.ndr.socialasteroids.utils.JwtUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthService
{
    private final @NonNull UserService userService;
    private final @NonNull AuthenticationManager authManager;
    private final @NonNull RefreshTokenService refreshTokenService;
    private final @NonNull JwtUtils jwtUtils;
    
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

    public ResponseCookie createRefreshTokenCookie(Long userId)
    {
        refreshTokenService.deleteByUserId(userId);
        
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userId);
        ResponseCookie cookie = jwtUtils.generateRefreshTokenCookie(refreshToken.getToken());
                 
        return cookie;
    }

    public ResponseCookie createJwt()
    {
        UserDetailsImpl userDetails = getUserDetailsImplOrElseThrow();
        ResponseCookie cookie = jwtUtils.generateJwtCookie(userDetails);

        return cookie;
    }

    public ResponseCookie authWithRefreshToken(String refreshTokenString)
    {
        RefreshToken refreshToken = refreshTokenService.findRefreshToken(refreshTokenString);
        UserDetailsImpl userDetails = getUserDetailsImplOrElseThrow();

        if (refreshToken.getUser().getId() != userDetails.getUserSecurityInfo().getId())
            throw new AccessDeniedException("Access denied");

        refreshTokenService.verifyExpiration(refreshToken);

        ResponseCookie cookie = jwtUtils.generateJwtCookie(userDetails);

        return cookie;
    }
    
    public void removeRefreshToken()
    {
        UserDetailsImpl userDetails = getUserDetailsImplOrElseThrow();

        refreshTokenService.deleteByUserId(userDetails.getUserSecurityInfo().getId());
    }

    private UserDetailsImpl getUserDetailsImplOrElseThrow()
    {
        var principal = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        //If not stance of UserDetails, its anonymous user
        if (!(principal instanceof UserDetails))
            throw new AccessDeniedException("Access denied");

        return (UserDetailsImpl) principal;
    }
}
