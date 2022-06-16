package com.ndr.socialasteroids.security.JWT;

import java.security.Key;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.ndr.socialasteroids.infra.error.exception.JwtException;
import com.ndr.socialasteroids.security.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils
{
    @Value("${sa.jwt.expiration-ms}")
    private static int jwtExpirationMs;

    @Value("${sa.jwt.cookie-name}")
    private static String jwtCookieName;

    @Value("${sa.jwt.cookie-path}")
    private static String jwtCookiePath;

    @Value("${sa.jwt.cookie-max-age}")
    private static long cookieMaxAge;

    private static Key jwtKey = Keys.hmacShaKeyFor("B@46a01a1569cdfa14332fwj3780409a1kk3h4d0oed".getBytes());

    public static String getJwtFromCookies(HttpServletRequest request)
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

    public static void eraseJwtCookie(HttpServletRequest request, HttpServletResponse response)
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

    public static ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal)
    {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookieName, jwt).path(jwtCookiePath).maxAge(cookieMaxAge)
                .httpOnly(true).build();

        return cookie;
    }

    public static ResponseCookie getCleanJwtCookie()
    {
        ResponseCookie cookie = ResponseCookie.from(jwtCookieName, null).path(jwtCookiePath).build();
        return cookie;
    }

    public static String getUsernameFromJwtToken(String jws)
    {
        return Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(jws).getBody().getSubject();
    }

    public static boolean validateJwtToken(String authToken)
    {
        try{
            Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e)
        {
            throw new JwtException("Invalid token");
        }
    }

    private static String generateTokenFromUsername(String username)
    {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(jwtKey)
                .compact();
    }
}
