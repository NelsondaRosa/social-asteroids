package com.ndr.socialasteroids.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ndr.socialasteroids.domain.entity.User;

import lombok.Data;

@Data
public class UserDetailsImpl implements UserDetails
{
    private UserSecurityDTO userSecurityInfo;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(UserSecurityDTO user)
    {
        this.userSecurityInfo = user;
    }

    public UserDetailsImpl(UserSecurityDTO user, Collection<? extends GrantedAuthority> authorities)
    {
        this.userSecurityInfo = user;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user)
    {
        List<GrantedAuthority> authorities =
            user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
            
        return new UserDetailsImpl(new UserSecurityDTO(user), authorities);    
    }

    @Override
    public String getUsername()
    {
        return userSecurityInfo.getUsername();
    }

    @Override
    public String getPassword(){
        return userSecurityInfo.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        // TODO Auto-generated method stub
        return true;
    }
}
