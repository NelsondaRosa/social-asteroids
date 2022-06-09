package com.ndr.socialasteroids.merchant.payload.response;

import com.ndr.socialasteroids.domain.entity.AppUser;

import lombok.Data;

@Data
public class AppUserRes
{
    private Long id;
    private String username;

    public AppUserRes(AppUser user)
    {
        this.id = user.getId();
        this.username = user.getUsername();
    }

}
