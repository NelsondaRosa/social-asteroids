package com.ndr.socialasteroids.presentation.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndr.socialasteroids.business.DTO.UserDTO;
import com.ndr.socialasteroids.business.service.UserService;
import com.ndr.socialasteroids.presentation.payload.request.user.UpdatePasswordRequest;
import com.ndr.socialasteroids.presentation.payload.request.user.UpdateUserInfoRequest;
import com.ndr.socialasteroids.security.entities.UserDetailsImpl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController 
{
    private final @NonNull UserService userService;

    @GetMapping(path = "/active")
    public ResponseEntity<?> getActiveUser()
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails)
        {
            UserDTO userDTO = new UserDTO(((UserDetailsImpl) principal).getUserSecurityInfo());
            return ResponseEntity.ok().body(userDTO);
        }
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping(path = "/update")
    @PreAuthorize("#user.id == principal.getUserSecurityInfo().getId()")
    public ResponseEntity<?> updateInfo(@P("user") @Valid @RequestBody UpdateUserInfoRequest request)
    {
        UserDTO updatedUser = userService.update(
                                            request.getId(),
                                            request.getUsername(),
                                            request.getEmail());

        return ResponseEntity.ok().body(updatedUser);
    }

    @PostMapping(path = "/update-password")
    @PreAuthorize("#user.id == principal.getUserSecurityInfo().getId()")
    public ResponseEntity<?> updatePassword(@P("user") @Valid @RequestBody UpdatePasswordRequest request)
    {
        UserDTO updatedUser = userService.updatePassword(
                                            request.getId(),
                                            request.getActualPassword(),
                                            request.getNewPassword());
        
        return ResponseEntity.ok().body(updatedUser);
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId)
    {
        UserDTO user = userService.getById(Long.valueOf(userId));

        return ResponseEntity.ok().body(user);
    }

    @GetMapping(path = "/search/{query}")
    public ResponseEntity<?> searchByUsername(@PathVariable String query)
    {
        List<UserDTO> users = userService.searchByUsername(query);

        if (users.size() <= 0)
        {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(users);
    }
}

