package com.ndr.socialasteroids.security.JWT;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ndr.socialasteroids.domain.entity.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>
{
    @Override
    Optional<RefreshToken> findById(Long id);

    Optional<RefreshToken> findByToken(String token);

    boolean deleteByUser(User user);
}
