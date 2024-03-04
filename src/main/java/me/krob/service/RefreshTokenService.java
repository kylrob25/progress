package me.krob.service;

import me.krob.model.token.RefreshToken;
import me.krob.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${progress.jwt.refreshExpiration}")
    private long refreshExpiration;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken create(String username){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);

        Instant now = Instant.now();
        refreshToken.setCreation(now);
        refreshToken.setExpiry(now.plusMillis(refreshExpiration));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verify(RefreshToken token){
        if (token.getExpiry().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            return null;
        }
        return token;
    }

    public void deleteByUserId(String username){
        refreshTokenRepository.deleteByUsername(username);
    }
}
