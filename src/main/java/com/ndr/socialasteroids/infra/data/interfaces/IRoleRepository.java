package com.ndr.socialasteroids.infra.data.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ndr.socialasteroids.domain.entities.Role;
import com.ndr.socialasteroids.domain.enums.ERole;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
