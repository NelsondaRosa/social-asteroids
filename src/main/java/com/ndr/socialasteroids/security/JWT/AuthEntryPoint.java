package com.ndr.socialasteroids.security.JWT;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint{

    Logger logger = LoggerFactory.getLogger(AuthEntryPoint.class);
    
    //Class/method to handle unauthenticated users trying to access protected resources
    //different purpose of ResponseErrorHandler
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException
    {
        logger.error("Unauthorized : {}", authException.getMessage());
        logger.error("Failed to access {}", request.getServletPath());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You don't have access to this resource, try to authenticate first.");
    }
    
}
