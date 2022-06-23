package com.ndr.socialasteroids.security.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ndr.socialasteroids.domain.entity.User;
import com.ndr.socialasteroids.infra.data.repository.UserRepository;
import com.ndr.socialasteroids.infra.error.exception.RefreshTokenException;
import com.ndr.socialasteroids.security.entities.RefreshToken;
import com.ndr.socialasteroids.security.repository.RefreshTokenRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RefreshTokenService
{
    @Value("${sa.jwt.refresh-expiration-ms}")
    private String refreshTokenDuration;

    private final @NonNull RefreshTokenRepository refreshTokenRepository;
    private final @NonNull UserRepository userRepository;
    
    public RefreshToken createRefreshToken(Long userId)
    {
        User user = userRepository.findById(userId).orElseThrow();
        RefreshToken refreshToken = new RefreshToken(user, Long.valueOf(refreshTokenDuration));

        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken)
    {
        if (refreshToken.isExpired())
        {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException("Refresh token is expired, please login again");
        }

        return refreshToken;
    }

    public RefreshToken getByToken(String token)
    {
        return refreshTokenRepository.findByToken(token).orElseThrow(() -> new RefreshTokenException("Refresh Token doesn't exist"));
    }

    public RefreshToken getByUser(User user)
    {
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user).orElse(null);
        return refreshToken;
    }

    @Transactional
    public Long deleteByUserId(Long userId)
    {
        User user = userRepository.findById(userId).orElseThrow();

        return refreshTokenRepository.deleteByUser(user);
    }
}
