package com.ndr.socialasteroids.security.service;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ndr.socialasteroids.security.utils.JwtUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthTokenFilter extends OncePerRequestFilter
{
    private final @NonNull UserDetailsServiceImpl userDetailsService;
    private final @NonNull JwtUtils jwtUtils;
    private final @NonNull RefreshTokenService refreshTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {    
        try
        {
            String jwt = jwtUtils.getJwtFromCookies(request);

            if (jwt != null)
            {
                jwtUtils.validateJwt(jwt);

                String username = jwtUtils.getUsernameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception exception) //If jwt is invalid, exception will be thrown and Jwt cookie erased
        {
            jwtUtils.eraseJwtCookie(request, response);
        }

        filterChain.doFilter(request, response);
    }
}
