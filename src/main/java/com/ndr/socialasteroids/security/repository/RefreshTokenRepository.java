package com.ndr.socialasteroids.security.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ndr.socialasteroids.domain.entity.User;
import com.ndr.socialasteroids.security.entities.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>
{
    @Override
    Optional<RefreshToken> findById(Long id);

    Optional<RefreshToken> findByToken(String token);

    Long deleteByUser(User user);

    Optional<RefreshToken> findByUser(User user);

    Optional<Set<RefreshToken>> findAllByUser(User user);
}
