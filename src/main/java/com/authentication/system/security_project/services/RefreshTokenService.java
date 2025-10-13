package com.authentication.system.security_project.services;

import com.authentication.system.security_project.dtos.RefreshTokenDto;
import com.authentication.system.security_project.dtos.TokensResponseDto;
import com.authentication.system.security_project.exceptions.RefreshTokenExpiredException;
import com.authentication.system.security_project.exceptions.TokenDoesNotExistsException;
import com.authentication.system.security_project.exceptions.UserDoesNotExistsException;
import com.authentication.system.security_project.models.RefreshToken;
import com.authentication.system.security_project.models.User;
import com.authentication.system.security_project.projections.RefreshTokenWithUserProjection;
import com.authentication.system.security_project.repositories.RefreshTokenRepository;
import com.authentication.system.security_project.repositories.UserReposirory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenService {
    private RefreshTokenRepository refreshTokenRepository;
    private UserReposirory userReposirory;
    private JwtService jwtService;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserReposirory userReposirory, JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userReposirory = userReposirory;
        this.jwtService = jwtService;
    }

    //private method which actually generated the tken
    private RefreshToken getRefreshToken(User user) {
        return RefreshToken.builder()
                           .token(UUID.randomUUID().toString())
                           .user(user)
                           .expiryDate(Instant.now().plusSeconds(24L * 60 * 60))
                           .build();
    }

    public RefreshToken generateRefreshToken(String email) {
        User user = userReposirory.findByEmail(email).orElseThrow(() -> new UserDoesNotExistsException("User with this email does not exists ,so failed to generate refresh token"));

        java.util.Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUser(user);
        refreshToken.ifPresent(token -> refreshTokenRepository.delete(token));
        RefreshToken newRefreshToken = getRefreshToken(user);
        return refreshTokenRepository.save(newRefreshToken);
    }

    public TokensResponseDto refreshTheToken(RefreshTokenDto tokenDto) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(tokenDto.getToken()).orElseThrow(() -> new TokenDoesNotExistsException("The refresh token does not exists please provide valid one"));
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenExpiredException("Your refresh token is expired,please login to get another one");
        } else {
            RefreshToken newRefreshToken = getRefreshToken(refreshToken.getUser());
            String accessToken = jwtService.generateToken(refreshToken.getUser().getEmail());
            refreshTokenRepository.delete(refreshToken);
            refreshTokenRepository.save(newRefreshToken);
            return TokensResponseDto.builder()
                                    .accessToken(accessToken)
                                    .refreshToken(newRefreshToken.getToken())
                                    .build();
        }
    }

    public void logout(RefreshTokenDto refreshTokenDto) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(refreshTokenDto.getToken());
        refreshToken.ifPresentOrElse(refreshTokenRepository::delete, () -> log.warn("The token is not present in db"));
    }

    public List<RefreshTokenWithUserProjection> getUserWithTokens() {
        return refreshTokenRepository.getTokenWithUser();
    }
}
