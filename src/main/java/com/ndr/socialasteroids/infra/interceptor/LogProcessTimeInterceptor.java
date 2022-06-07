package com.ndr.socialasteroids.infra.interceptor;

import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LogProcessTimeInterceptor implements HandlerInterceptor {

    private long startTime = 0;
    private long processedTime = 0;
    private Logger logger = LoggerFactory.getLogger(LogProcessTimeInterceptor.class);

    public LogProcessTimeInterceptor(){
        logger.info("LogProcessTimeInterceptor initialized");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        long currentTime = System.currentTimeMillis();
        logger.info("\n" + request.getRequestURI() + "\nProcessed in:    " + TimeUnit.MILLISECONDS.toMillis(processedTime - startTime) + " millis\n"
                    + "Completition in: " + TimeUnit.MILLISECONDS.toMillis(currentTime - startTime) + " millis");

        startTime = 0;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
            processedTime = System.currentTimeMillis();

            

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        startTime = System.currentTimeMillis();

        logger.info("\nJSESSIONID: " + request.getSession().getId());
            logger.info("\nSESSION CREATION TIME: " + request.getSession().getCreationTime());
            logger.info("\nLAST ACCESSED TIME: " + request.getSession().getLastAccessedTime());
            logger.info("\nREAL PATH OF /USER: " + request.getSession().getServletContext().getRealPath("/user"));
            Enumeration<String> attributes = request.getSession().getAttributeNames();
            String attString = "";
            while(attributes.hasMoreElements()){
                attString += "\n" + attributes.nextElement();
            }
            logger.info("\nAttributes: " + attString);

            SecurityContext sc =(SecurityContext) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
            if(sc != null){
                logger.info(sc.toString());
            }
            
            Integer reqcount = (Integer) request.getSession().getAttribute("reqcount");
            if(reqcount == null){
                request.getSession().setAttribute("reqcount", 1);
            } else {
                request.getSession().setAttribute("reqcount", ++reqcount);
            }

            logger.info("\nJA ACESSOU SA MERDA JA MANO: " + request.getSession().getAttribute("reqcount"));

            logger.info("\nVAMO TE DESLIGAR SE TU FICAR FORA ISSO AQUI VEI " + request.getSession().getMaxInactiveInterval());

        return true;
    }
    
}
