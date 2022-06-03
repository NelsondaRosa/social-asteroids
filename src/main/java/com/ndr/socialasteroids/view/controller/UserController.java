package com.ndr.socialasteroids.view.controller;

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

import com.ndr.socialasteroids.domain.entities.AppUser;
import com.ndr.socialasteroids.service.UserService;
import com.ndr.socialasteroids.view.dto.UserDTO;
import com.ndr.socialasteroids.view.viewobject.UserVO;

@RestController
@RequestMapping("/user")
//TODO:: Login endpoint
public class UserController {
    
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;

    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO){
        AppUser user = userDTO.toDomainEntity();
        try{
            userService.register(user);
            
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "get/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId){
        AppUser user;
        try{
            user = userService.getById(Long.valueOf(userId));
        } catch (EntityNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.badRequest().build();
        }

        UserVO userVO = new UserVO(user);

        return ResponseEntity.ok().body(userVO);
    }


}
