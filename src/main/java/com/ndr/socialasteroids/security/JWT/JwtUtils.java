package com.ndr.socialasteroids.security.JWT;

import java.security.Key;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.ndr.socialasteroids.infra.error.exception.JwtException;
import com.ndr.socialasteroids.security.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils
{
    @Value("${sa.jwt.expiration-ms}")
    private int jwtExpirationMs;

    @Value("${sa.jwt.cookie-name}")
    private String jwtCookieName;

    @Value("${sa.jwt.cookie-path}")
    private String jwtCookiePath;

    @Value("${sa.jwt.cookie-max-age}")
    private long cookieMaxAge;

    private Key jwtKey = Keys.hmacShaKeyFor("B@46a01a1569cdfa14332fwj3780409a1kk3h4d0oed".getBytes());

    private Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    public String getJwtFromCookies(HttpServletRequest request)
    {
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);

        if (cookie != null)
        {
            return cookie.getValue();
        } else
        {
            return null;
        }
    }

    public void eraseJwtCookie(HttpServletRequest request, HttpServletResponse response)
    {
        //TODO:: getCookie está dando exceção para cookie não existente, deveria retornar null, verificar isso
        try{
            Cookie cookie = WebUtils.getCookie(request, jwtCookieName);

            if (cookie != null)
            {
                cookie.setValue(null);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        } catch(NullPointerException ex) {
            return;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal)
    {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookieName, jwt).path(jwtCookiePath).maxAge(cookieMaxAge)
                .httpOnly(true).build();

        return cookie;
    }

    public ResponseCookie getCleanJwtCookie()
    {
        ResponseCookie cookie = ResponseCookie.from(jwtCookieName, null).path(jwtCookiePath).build();
        return cookie;
    }

    public String getUsernameFromJwtToken(String jws)
    {
        return Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(jws).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken)
    {
        try{
            Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(authToken);
            return true;
        // } catch (SignatureException e) {
        //     logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e)
        {
            throw new JwtException("Invalid token");
        }
        
        return false;
    }

    private String generateTokenFromUsername(String username)
    {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(jwtKey)
                .compact();
    }
}
