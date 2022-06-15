package com.ndr.socialasteroids.security.JWT;

import java.security.Key;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

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
    //@Value("${sa.jwt.secret}")
    //private String stringKey;
    @Value("${sa.jwt.expiration-ms}")
    private int jwtExpirationMs;
    @Value("${sa.jwt.cookie-name}")
    private String jwtCookieName;
    @Value("${sa.jwt.cookie-path}")
    private String cookiePath;
    @Value("${sa.jwt.cookie-max-age}")
    private long cookieMaxAge;

    private Key jwtKey;

    public JwtUtils()
    {
        jwtKey = Keys.hmacShaKeyFor("B@46a0ef6fasdfqwwfdsfasdasdewqeasfxgsdgqwdwasfsdgqwasd".getBytes());
    }

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

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal)
    {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookieName, jwt).path(cookiePath).maxAge(cookieMaxAge)
                .httpOnly(true).build();

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
        } catch (Exception e)
        {
            throw new JwtException("Invalid token");
        }
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
