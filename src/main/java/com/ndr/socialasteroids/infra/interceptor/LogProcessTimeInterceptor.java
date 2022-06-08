package com.ndr.socialasteroids.infra.interceptor;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LogProcessTimeInterceptor implements HandlerInterceptor {

    private long startTime = 0;
    private long processedTime = 0;
    private Logger logger = LoggerFactory.getLogger(LogProcessTimeInterceptor.class);

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)throws Exception {
        long currentTime = System.currentTimeMillis();
        logger.info("\n" + request.getRequestURI() + "\nProcessed in:    " + TimeUnit.MILLISECONDS.toMillis(processedTime - startTime) + " millis\n"
                    + "Completition in: " + TimeUnit.MILLISECONDS.toMillis(currentTime - startTime) + " millis");
        startTime = 0;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        processedTime = System.currentTimeMillis();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
        startTime = System.currentTimeMillis();
        return true;
    }
    
}
