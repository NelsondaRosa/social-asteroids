package com.ndr.socialasteroids.merchant.payload.request;

import com.ndr.socialasteroids.domain.entity.AppUser;

import lombok.Data;

@Data
public class UserReq
{
    private String username;
    private String email;
    private String password;

    public UserReq(AppUser user)
    {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public UserReq(){}

    public AppUser toDomainEntity()
    {
        return new AppUser(this.username, this.email, this.password);
    }
}
