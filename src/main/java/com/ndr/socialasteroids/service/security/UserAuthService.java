package com.ndr.socialasteroids.service.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ndr.socialasteroids.domain.entities.AppUser;
import com.ndr.socialasteroids.domain.entities.AppUserDetails;
import com.ndr.socialasteroids.infra.data.interfaces.IUserRepository;
import com.ndr.socialasteroids.view.dto.AppUserDTO;

@Service
public class UserAuthService implements UserDetailsService {

    private final IUserRepository userRepository;

    @Autowired
    public UserAuthService(IUserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Cannot find user " + username);
        }

        UserDetails userDetails = new AppUserDetails(new AppUserDTO(user));

        return userDetails;
    }
    
}
