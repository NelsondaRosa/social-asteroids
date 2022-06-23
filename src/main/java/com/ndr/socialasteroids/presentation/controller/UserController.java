package com.ndr.socialasteroids.presentation.controller;

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
import com.ndr.socialasteroids.presentation.payload.request.user.UpdatePasswordRequest;
import com.ndr.socialasteroids.presentation.payload.request.user.UpdateUserInfoRequest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController 
{
    private final @NonNull UserService userService;

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

