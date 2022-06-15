package com.ndr.socialasteroids.presentation.controller;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndr.socialasteroids.business.DTOs.UserDTO;
import com.ndr.socialasteroids.business.service.UserService;
import com.ndr.socialasteroids.presentation.payload.request.user.CreateUserRequest;
import com.ndr.socialasteroids.presentation.payload.request.user.LoginRequest;
import com.ndr.socialasteroids.presentation.payload.request.user.UpdatePasswordRequest;
import com.ndr.socialasteroids.presentation.payload.request.user.UpdateUserInfoRequest;

@RestController
@RequestMapping("/user")
public class UserController 
{
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
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
        UserDTO newUser = 
            userService.createUser(request.getUsername(), request.getEmail(), request.getPassword());

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping(path = "/update")
    @PreAuthorize("#user.id == principal.getUserSecurityInfo().getId()")
    public ResponseEntity<?> updateInfo(@P("user") @RequestBody UpdateUserInfoRequest request)
    {
        UserDTO updatedUser = userService.update(
                                            request.getId(),
                                            request.getUsername(),
                                            request.getEmail());

        return ResponseEntity.ok().body(updatedUser);
    }

    @PostMapping(path = "/update-password")
    @PreAuthorize("#user.id == principal.getUserSecurityInfo().getId()")
    public ResponseEntity<?> updatePassword(@P("user") @RequestBody UpdatePasswordRequest request)
    {
        UserDTO updatedUser = userService.updatePassword(
                                            request.getId(),
                                            request.getActualPassword(),
                                            request.getNewPassword());
        
        return ResponseEntity.ok().body(updatedUser);
    }

    @GetMapping(path = "get/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String userId)
    {
        UserDTO user = userService.getById(Long.valueOf(userId));

        return ResponseEntity.ok().body(user);
    }
}
