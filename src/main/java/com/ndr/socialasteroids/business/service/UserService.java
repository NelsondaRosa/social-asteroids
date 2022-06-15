package com.ndr.socialasteroids.business.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ndr.socialasteroids.business.DTOs.UserDTO;
import com.ndr.socialasteroids.domain.entity.Role;
import com.ndr.socialasteroids.domain.entity.User;
import com.ndr.socialasteroids.domain.enums.ERole;
import com.ndr.socialasteroids.infra.data.repository.RoleRepository;
import com.ndr.socialasteroids.infra.data.repository.UserRepository;
import com.ndr.socialasteroids.infra.error.exception.DuplicateValueException;
import com.ndr.socialasteroids.security.UserDetailsImpl;
import com.ndr.socialasteroids.security.JWT.JwtUtils;

@Service
public class UserService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, AuthenticationManager authManager, JwtUtils jwtUtils)
    {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
    }

    public UserDTO authenticateUser(String username, String password)
    {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        UserDTO user = new UserDTO(userDetails.getUserSecurityInfo());
        return user;
    }

    public ResponseCookie createJwtCookie()
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        return jwtCookie;
    }
    
    public UserDTO createUser(String username, String email, String password)
        throws NoSuchElementException, DuplicateValueException
    {
        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email))
        {
            throw new DuplicateValueException("Username or email alredy exist");
        }

        User user = new User(username, email, passwordEncoder.encode(password));
        
        //TODO: arrumar atribuição de role
        Role role = roleRepository.findByName(ERole.ROLE_USER).orElseThrow();
        user.addRole(role);

        UserDTO createdUser = new UserDTO(userRepository.saveAndFlush(user));
        
        return createdUser;
    }

    public UserDTO getById(Long userId) throws NoSuchElementException
    {
        User user = userRepository.findById(userId).orElseThrow();
        UserDTO userDTO = new UserDTO(user);

        return userDTO;
    }

    public UserDTO update(Long id, String username, String email)
        throws NoSuchElementException
    {
        User userToUpdate = userRepository.findById(id).orElseThrow();
        userToUpdate.setEmail(email);
        userToUpdate.setUsername(username);

        UserDTO updatedUserDTO = new UserDTO(userRepository.saveAndFlush(userToUpdate));

        return updatedUserDTO;
    }
    
    public UserDTO updatePassword(Long userId, String actualPassword, String newPassword) throws NoSuchElementException
    {
        User userToUpdate = userRepository.findById(userId).orElseThrow();
        String actualPasswordEncoded = passwordEncoder.encode(actualPassword);

        if (actualPasswordEncoded != userToUpdate.getPassword())
        {
            throw new AccessDeniedException("Password mismatch");
        }
    
        userToUpdate.setPassword(passwordEncoder.encode(newPassword));
        UserDTO updatedUserDTO = new UserDTO(userRepository.saveAndFlush(userToUpdate));

        return updatedUserDTO;
    }
    
    //Protected method only to be used in the service package. Returns domain entity
    protected User getEntityById(Long userId) throws NoSuchElementException
    {
        User user = userRepository.findById(userId).orElseThrow();

        return user;
    }
    
}
