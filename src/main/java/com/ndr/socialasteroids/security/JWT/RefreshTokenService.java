package com.ndr.socialasteroids.security.JWT;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ndr.socialasteroids.domain.entity.User;
import com.ndr.socialasteroids.infra.data.repository.UserRepository;
import com.ndr.socialasteroids.infra.error.exception.RefreshTokenException;

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

    public RefreshToken findRefreshToken(String token)
    {
        return refreshTokenRepository.findByToken(token).orElseThrow(() -> new RefreshTokenException("Refresh Token doesn't exist"));
    }
    
    public RefreshToken createRefreshToken(Long userId)
    {
        RefreshToken refreshToken = new RefreshToken();
        User user = userRepository.findById(userId).orElseThrow();
        
        //TODO:: Colocar em construtor todas propriedades obrigatorias
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(Long.valueOf(refreshTokenDuration)));
        refreshToken.setToken(UUID.randomUUID().toString());

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

    public void deleteByUserId(Long userId)
    {
        User user = userRepository.findById(userId).orElseThrow();
        refreshTokenRepository.deleteByUser(user);
    }


    
}
