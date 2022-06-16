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
import com.ndr.socialasteroids.business.service.UserService;
import com.ndr.socialasteroids.presentation.payload.request.user.CreateUserRequest;
import com.ndr.socialasteroids.presentation.payload.request.user.LoginRequest;
import com.ndr.socialasteroids.security.JWT.JwtUtils;

@RestController
@RequestMapping("/auth")
public class AuthController
{
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(UserService userService, JwtUtils jwtUtils)
    {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }
    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest)
    {
        UserDTO user = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        ResponseCookie cookie = userService.createJwtCookie();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(user);
    }
    
    @PostMapping(path = "/signup")
    public ResponseEntity<?> signup(@RequestBody CreateUserRequest request) throws URISyntaxException
    {
        UserDTO newUser = userService.createUser(request.getUsername(), request.getEmail(), request.getPassword());

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
    
    @GetMapping(path = "/logout")
    public ResponseEntity<?> logout()
    {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }   
}
