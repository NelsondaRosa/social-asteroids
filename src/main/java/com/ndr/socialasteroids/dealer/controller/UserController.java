package com.ndr.socialasteroids.dealer.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndr.socialasteroids.dealer.payload.request.UserReq;
import com.ndr.socialasteroids.dealer.payload.response.AppUserRes;
import com.ndr.socialasteroids.domain.entity.AppUser;
import com.ndr.socialasteroids.service.UserService;

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
    public ResponseEntity<?> register(@RequestBody UserReq userReq) throws URISyntaxException
    {
        AppUser user = userReq.toDomainEntity();
        AppUser userCreated = userService.register(user);

        //TODO: passar a lógica da hypermedia para o service
        return ResponseEntity.created(new URI("localhost:8080/user/get/" + userCreated.getId())).build();
    }

    @GetMapping(path = "get/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId)
    {
        AppUser user;
        try{
            user = userService.getById(Long.valueOf(userId));
        } catch (EntityNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.badRequest().build();
        }

        AppUserRes userRes = new AppUserRes(user);

        return ResponseEntity.ok().body(userRes);
    }
}
