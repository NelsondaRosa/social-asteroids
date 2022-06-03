package com.ndr.socialasteroids.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ndr.socialasteroids.domain.entities.AppUser;
import com.ndr.socialasteroids.domain.entities.Role;
import com.ndr.socialasteroids.domain.enums.ERole;
import com.ndr.socialasteroids.infra.data.interfaces.IRoleRepository;
import com.ndr.socialasteroids.infra.data.interfaces.IUserRepository;

@Service
public class UserService {
    
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;

    @Autowired
    public UserService(IUserRepository userRepository, PasswordEncoder passwordEncoder, IRoleRepository roleRepository){
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(AppUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName(ERole.ROLE_USER).get();
        user.addRole(role);
        userRepository.saveAndFlush(user);
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
