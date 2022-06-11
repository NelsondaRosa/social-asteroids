package com.ndr.socialasteroids.presentation.controller;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.ndr.socialasteroids.presentation.payload.request.RegisterUserRequest;
import com.ndr.socialasteroids.presentation.payload.request.UpdateUserRequest;
import com.ndr.socialasteroids.presentation.payload.response.Pair;
import com.ndr.socialasteroids.presentation.payload.response.ResponseHandler;

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

    @GetMapping(path = "/login")
    public ResponseEntity<?> login()
    { //TODO
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserRequest userReq) throws URISyntaxException
    {
        UserDTO newUser = 
            userService.register(userReq.getUsername(), userReq.getEmail(), userReq.getPassword());
        
        var responseBody = ResponseHandler.createResponseBody(
          Pair.build("id",newUser.getId()),
          Pair.build("username",newUser.getUsername()),
          Pair.build("email",newUser.getEmail())  
        );

        return ResponseEntity.ok().body(responseBody);
        //TODO: passar a lógica da hypermedia para o service
        //return ResponseEntity.created(new URI("localhost:8080/user/get/" + newUser.getId())).build();
    }

    @PostMapping(path = "/update")
    @PreAuthorize("#u.id == principal.getUser().getId()")
    public ResponseEntity<?> update(@P("u") @RequestBody UpdateUserRequest userReq)
    {
        UserDTO updatedUser = userService.update(userReq.getId(), userReq.getUsername(), userReq.getEmail());

        var responseBody = ResponseHandler.createResponseBody(
            Pair.build("username", updatedUser.getUsername()),
            Pair.build("email", updatedUser.getEmail()));

        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping(path = "get/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String userId)
    {
        UserDTO user = userService.getById(Long.valueOf(userId));

        return ResponseEntity.ok().body(user);
    }
}
