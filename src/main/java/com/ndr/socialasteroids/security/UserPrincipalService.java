package com.ndr.socialasteroids.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ndr.socialasteroids.domain.entity.AppUser;
import com.ndr.socialasteroids.infra.data.repository.UserRepository;

@Service
public class UserPrincipalService implements UserDetailsService
{

    private final UserRepository userRepository;

    @Autowired
    public UserPrincipalService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        AppUser user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Cannot find user " + username);
        }

        UserDetails userDetails = new UserPrincipal(new AppUserDTO(user));

        return userDetails;
    }
    
}
