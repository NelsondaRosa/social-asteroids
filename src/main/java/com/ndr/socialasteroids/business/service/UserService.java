package com.ndr.socialasteroids.business.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ndr.socialasteroids.business.DTO.UserDTO;
import com.ndr.socialasteroids.domain.entity.Role;
import com.ndr.socialasteroids.domain.entity.User;
import com.ndr.socialasteroids.domain.enums.ERole;
import com.ndr.socialasteroids.infra.data.repository.RoleRepository;
import com.ndr.socialasteroids.infra.data.repository.UserRepository;
import com.ndr.socialasteroids.infra.error.exception.DuplicateValueException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService
{
    private final @NonNull UserRepository userRepository;
    private final @NonNull PasswordEncoder passwordEncoder;
    private final @NonNull RoleRepository roleRepository;

    public UserDTO createUser(String username, String email, String password)
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

    //TODO: make pageable
    public List<UserDTO> searchByUsername(String search)
    {
        List<UserDTO> users = new ArrayList<UserDTO>();

        userRepository.searchByUsername(search)
                        .stream()
                        .forEach(user -> users.add(new UserDTO(user)));
    
        return users;
    }
    
    public User getEntityById(Long userId) throws NoSuchElementException
    {
        User user = userRepository.findById(userId).orElseThrow();

        return user;
    }
    
}
