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

import com.ndr.socialasteroids.infra.error.exception.JwtException;
import com.ndr.socialasteroids.security.UserDetailsServiceImpl;

@Component
public class AuthTokenFilter extends OncePerRequestFilter
{
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthTokenFilter(UserDetailsServiceImpl userDetailsService, JwtUtils jwtUtils)
    {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        try
        {   
            String jwt = jwtUtils.getJwtFromCookies(request);

            if (jwt != null && jwtUtils.validateJwtToken(jwt))
            {
                String username = jwtUtils.getUsernameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = 
                                                        new UsernamePasswordAuthenticationToken(
                                                            userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e)
        {
            //TODO\; Apagar token quando cair aqui. adicionar isso ao response. lançar exceção(?)
            response.sendError(401);
        }

        filterChain.doFilter(request, response);
    }
}
