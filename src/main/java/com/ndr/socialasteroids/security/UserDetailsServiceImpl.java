package com.ndr.socialasteroids.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ndr.socialasteroids.domain.entity.User;
import com.ndr.socialasteroids.infra.data.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findByUsername(username).orElseThrow();
        
        if(user == null){
            throw new UsernameNotFoundException("Cannot find user " + username);
        }

        UserDetails userDetails = new UserDetailsImpl(new UserSecurityDTO(user));

        return userDetails;
    }
    
}
