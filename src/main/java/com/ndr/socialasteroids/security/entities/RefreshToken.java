package com.ndr.socialasteroids.security.entities;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.ndr.socialasteroids.domain.entity.User;

import lombok.Data;

@Data
@Entity
@Table(name = "refresh_token",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "token"),
    })
public class RefreshToken
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @NotNull
    private User user;

    @Column
    @NotNull
    private String token;

    @Column
    @NotNull
    private Instant expiryDate;

    public RefreshToken(User  user, Long durationInMillis)
    {
        this.user = user;
        this.token = UUID.randomUUID().toString();
        this.expiryDate = Instant.now().plusMillis(durationInMillis);
    }
    
    public RefreshToken(){}

    public boolean isExpired()
    {
        if (expiryDate.compareTo(Instant.now()) <= 0)
        {
            return true;
        }
        return false;
    }
}
