package com.ndr.socialasteroids.infra.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ndr.socialasteroids.domain.entity.Role;
import com.ndr.socialasteroids.domain.enums.ERole;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>
{
    Optional<Role> findByName(ERole name);
}
