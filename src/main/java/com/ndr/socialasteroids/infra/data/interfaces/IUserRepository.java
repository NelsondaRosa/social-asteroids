package com.ndr.socialasteroids.infra.data.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ndr.socialasteroids.domain.entities.AppUser;

@Repository
public interface IUserRepository extends JpaRepository<AppUser, Long>{
    AppUser findByUsername(String username);
    
}
