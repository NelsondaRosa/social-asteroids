package com.ndr.socialasteroids.security.service;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ndr.socialasteroids.infra.error.exception.JwtException;
import com.ndr.socialasteroids.security.entities.RefreshToken;
import com.ndr.socialasteroids.security.entities.UserDetailsImpl;
import com.ndr.socialasteroids.security.utils.JwtUtils;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthTokenFilter extends OncePerRequestFilter
{
    private final @NonNull UserDetailsServiceImpl userDetailsService;
    private final @NonNull JwtUtils jwtUtils;
    private final @NonNull RefreshTokenService refreshTokenService;

    /**
     * Filter to be called before every request.
     * Will check the existence of a JWT in the request.
     * If the token exists and is invalid (expired, incorrect values ​​etc) it will try to authenticate via RefreshToken. 
     * If the refresh token is invalid, delete the possible JWT cookie value on the client.
     * <p>
     * Finally, it will proceed to the filter chair.
     * <p>
     * It does not add HttpStatus.UNAUTHORIZED to response because this is done by other parts of the application with Spring Security.
     * If it did, endpoints accessible by everyone would be blocked.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        RefreshToken refreshToken = null;

        try
        {
            String jwt = jwtUtils.getJwtFromCookie(request);

            try
            {
                if (jwt == null)
                    throw new JwtException();

                jwtUtils.validateJwt(jwt);

            } catch (ExpiredJwtException | JwtException ex)
            {
                //If jwt token is expired, get refreshToken by the cookie
                String refreshTokenString = jwtUtils.getRefreshTokenFromCookie(request);
                refreshToken = refreshTokenService.getByToken(refreshTokenString);

                if (refreshTokenString == null)
                {
                    throw ex;
                }

                //If refreshToken has expired, throw exception to the next catch bloc to invalidate auth proccess
                refreshTokenService.verifyExpiration(refreshToken);
            }

            //If refreshToken has been populated, get username by him, otherwise, get username by jwt claims
            String username = (refreshToken != null) ? refreshToken.getUser().getUsername()
                    : jwtUtils.getUsernameFromJwtToken(jwt);

            //Populate SecurityContextHolder with owner of jwt or refresh token
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //If refresh toke has been populated, create new jwt and set with response cookie
            if (refreshToken != null)
            {
                ResponseCookie newJwtCookie = jwtUtils.generateJwtCookie((UserDetailsImpl) userDetails);
                response.setHeader(HttpHeaders.SET_COOKIE, newJwtCookie.toString());
            }

        } 
        catch (Exception ex) //If jwt and refresh token is invalid, exception will be thrown and Jwt cookie erased
        {
            if (refreshToken != null)
            {
                refreshTokenService.deleteByToken(refreshToken.getToken());
            }
        }

        filterChain.doFilter(request, response);
    }
}
