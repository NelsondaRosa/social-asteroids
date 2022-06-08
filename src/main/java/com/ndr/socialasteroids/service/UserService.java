package com.ndr.socialasteroids.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ndr.socialasteroids.domain.entity.AppUser;
import com.ndr.socialasteroids.domain.entity.Role;
import com.ndr.socialasteroids.domain.enums.ERole;
import com.ndr.socialasteroids.infra.data.repository.RoleRepository;
import com.ndr.socialasteroids.infra.data.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository){
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser register(AppUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName(ERole.ROLE_USER).get();
        user.addRole(role);
        return userRepository.saveAndFlush(user);
    }

    public AppUser getById(Long userId) throws EntityNotFoundException{
        Optional<AppUser> user = userRepository.findById(userId);

        return user.get();
    }

    public AppUser update(AppUser user) throws NoSuchElementException{
        if(!userRepository.existsById(user.getId())){
            throw new NoSuchElementException();
        }
        
        AppUser updatedUser = userRepository.save(user);
 
        return updatedUser;
    }
    
}
