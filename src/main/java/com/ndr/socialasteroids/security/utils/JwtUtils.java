package com.ndr.socialasteroids.security.utils;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.ndr.socialasteroids.security.encoding.Encrypter;
import com.ndr.socialasteroids.security.entities.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtils
{
    @Value("${sa.jwt.expiration-sec}")
    private long jwtExpirationMs;

    @Value("${sa.jwt.cookie-path}")
    private String cookiePath;

    @Value("${sa.jwt.refresh-token-cookie-name}")
    private String refreshTokenCookieName;

    @Value("${sa.jwt.jwt-cookie-name}")
    private String jwtCookieName;

    @Value("${sa.jwt.jwt-cookie-max-age-sec}")
    private long jwtCookieMaxAge;

    @Value("${sa.jwt.refresh-token-cookie-max-age-sec}")
    private long refreshTokenCookieMaxAge;

    @Value("${sa.jwt.secret}")
    private String secret;

    public String getJwtFromCookie(HttpServletRequest request)
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

    public String getRefreshTokenFromCookie(HttpServletRequest request)
    {
        Cookie cookie = WebUtils.getCookie(request, refreshTokenCookieName);

        if (cookie != null)
        {
            String refreshTokenEncrypted = cookie.getValue();
            String refreshTokenString = Encrypter.decrypt(refreshTokenEncrypted);
            
            return refreshTokenString;
        } else
        {
            return null;
        }
    }

    public void eraseJwtCookie(HttpServletResponse response)
    {
        Cookie cookie = new Cookie(jwtCookieName, null);
        cookie.setPath(cookiePath);

        response.addCookie(cookie);
    }

    public void eraseRefreshTokenCookie(HttpServletResponse response)
    {
        Cookie cookie = new Cookie(refreshTokenCookieName, null);
        cookie.setPath(cookiePath);

        response.addCookie(cookie);
    }

    public void eraseAllJwtRelatedCookies(HttpServletResponse response)
    {
        eraseJwtCookie(response);
        eraseRefreshTokenCookie(response);
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal)
    {
        System.out.println(jwtCookieMaxAge);
        String jwt = generateToken(userPrincipal.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookieName, jwt).path(cookiePath).maxAge(Duration.ofSeconds(jwtCookieMaxAge))
                .sameSite("lax").httpOnly(true).build();

        return cookie;
    }
    
    public ResponseCookie generateRefreshTokenCookie(String token)
    {
        System.out.println(refreshTokenCookieMaxAge);
        ResponseCookie cookie = ResponseCookie.from(refreshTokenCookieName, token).path(cookiePath)
                .maxAge(Duration.ofSeconds(refreshTokenCookieMaxAge)).sameSite("lax").httpOnly(true).build();

        return cookie;
    }

    public ResponseCookie getCleanJwtCookie()
    {
        ResponseCookie cookie = ResponseCookie.from(jwtCookieName, null).path(cookiePath).build();
        return cookie;
    }

    public ResponseCookie getCleanRefreshTokenCookie()
    {
        ResponseCookie cookie = ResponseCookie.from(refreshTokenCookieName, null).path(cookiePath).build();
        return cookie;
    }

    public String getUsernameFromJwtToken(String jws)
    {
        Key jwtKey = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(jws).getBody().getSubject();
    }

    public void validateJwt(String authToken) throws MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException, SignatureException
    {
            Key jwtKey = Keys.hmacShaKeyFor(secret.getBytes());

            Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(authToken);
    }

    public String generateToken(String username)
    {
        Key jwtKey = Keys.hmacShaKeyFor(secret.getBytes());
        
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(jwtExpirationMs, ChronoUnit.MILLIS)))
                .signWith(jwtKey)
                .compact();
    }
}
