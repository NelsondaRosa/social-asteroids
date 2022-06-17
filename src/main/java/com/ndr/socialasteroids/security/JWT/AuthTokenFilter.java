package com.ndr.socialasteroids.security.JWT;

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

import com.ndr.socialasteroids.security.UserDetailsServiceImpl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthTokenFilter extends OncePerRequestFilter
{
    private final @NonNull UserDetailsServiceImpl userDetailsService;
    private final @NonNull JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        try
        {   
            String jwt = jwtUtils.getJwtFromCookies(request);

            if (jwt != null)
            {
                if(jwtUtils.validateJwtToken(jwt))
                {
                    String username = jwtUtils.getUsernameFromJwtToken(jwt);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = 
                                                            new UsernamePasswordAuthenticationToken(
                                                                userDetails, null, userDetails.getAuthorities());
    
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    jwtUtils.eraseJwtCookie(request, response);
                }
            }
        } catch (Exception e)
        {
            jwtUtils.eraseJwtCookie(request, response);
        }

        filterChain.doFilter(request, response);
    }
}
