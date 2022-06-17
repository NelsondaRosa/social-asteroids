package com.ndr.socialasteroids.presentation.controller;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndr.socialasteroids.business.DTOs.UserDTO;
import com.ndr.socialasteroids.business.service.AuthService;
import com.ndr.socialasteroids.business.service.UserService;
import com.ndr.socialasteroids.presentation.payload.request.user.CreateUserRequest;
import com.ndr.socialasteroids.presentation.payload.request.user.LoginRequest;
import com.ndr.socialasteroids.security.JWT.JwtUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController
{
    private final @NonNull AuthService authService;
    private final @NonNull UserService userService;
    private final @NonNull JwtUtils jwtUtils;

    @PostMapping(path = "/signup")
    public ResponseEntity<?> signup(@RequestBody CreateUserRequest request) throws URISyntaxException
    {
        UserDTO newUser = userService.createUser(request.getUsername(), request.getEmail(), request.getPassword());

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping(path = "/refresh-token")
    //@PreAuthorize("#userId == principal.getUserSecurityInfo().getId()")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken)
    {
        ResponseCookie cookie = authService.generateJwtCookieFromRefreshToken(refreshToken);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }
    
    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest)
    {
        UserDTO user = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        ResponseCookie cookie = authService.createJwtCookie();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString(), cookie.toString()).body(user);
    }
    
    @GetMapping(path = "/logout")
    public ResponseEntity<?> logout()
    {
        //TODO:: Passar logout para service
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }
}
