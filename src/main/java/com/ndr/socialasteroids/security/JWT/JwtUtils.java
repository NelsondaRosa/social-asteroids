package com.ndr.socialasteroids.security.JWT;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

import com.ndr.socialasteroids.security.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

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
        String jwt = generateToken(userPrincipal.getUsername());
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

    public void validateJwt(String authToken) throws MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException, SignatureException
    {
        Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(authToken);
    }

    public String generateToken(String username)
    {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(Long.valueOf(jwtExpirationMs), ChronoUnit.MILLIS)))
                .signWith(jwtKey)
                .compact();
    }
}
