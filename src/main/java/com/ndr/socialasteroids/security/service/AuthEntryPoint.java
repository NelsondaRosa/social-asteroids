package com.ndr.socialasteroids.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndr.socialasteroids.security.utils.JwtUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthEntryPoint implements AuthenticationEntryPoint
{
    private final @NonNull JwtUtils jwtUtils;
    Logger logger = LoggerFactory.getLogger(AuthEntryPoint.class);
    
    //Class/method to handle unauthenticated users trying to access protected resources
    //different purpose of ResponseErrorHandler - normal behaviour is redirect to /login
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException
    {
        
        ObjectMapper mapper = new ObjectMapper();

        jwtUtils.eraseAllJwtRelatedCookies(response);

        response.getWriter().write(mapper.writeValueAsString("You don't have access to this resource, try to authenticate first."));
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
    
}
