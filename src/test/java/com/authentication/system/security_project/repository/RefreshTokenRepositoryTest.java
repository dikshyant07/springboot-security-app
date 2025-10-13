package com.authentication.system.security_project.repository;

import com.authentication.system.security_project.enums.Gender;
import com.authentication.system.security_project.enums.Role;
import com.authentication.system.security_project.models.RefreshToken;
import com.authentication.system.security_project.models.User;
import com.authentication.system.security_project.repositories.RefreshTokenRepository;
import com.authentication.system.security_project.repositories.UserReposirory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.UUID;

@DataJpaTest
public class RefreshTokenRepositoryTest {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserReposirory userReposirory;

    @Autowired
    public RefreshTokenRepositoryTest(RefreshTokenRepository refreshTokenRepository, UserReposirory userReposirory) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userReposirory = userReposirory;
    }

    @Test
    void testRefreshTokensMethods() {
        User user = User.builder()
                        .name("John Doe")
                        .age(30)
                        .gender(Gender.MALE)
                        .email("john.doe@example.com")
                        .password("password123")
                        .role(Role.USER)
                        .build();
        User savedUser = userReposirory.save(user);
        Assertions.assertNotNull(savedUser);
        RefreshToken refreshToken = RefreshToken.builder()
                                                .token(UUID.randomUUID().toString())
                                                .expiryDate(Instant.now().plusSeconds(3600))
                                                .user(user)
                                                .build();
        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);

        Assertions.assertNotNull(savedToken);
        Assertions.assertEquals("john.doe@example.com", savedToken.getUser().getEmail());
        Assertions.assertNotNull(refreshTokenRepository.findByToken(savedToken.getToken()));
        Assertions.assertNotNull(refreshTokenRepository.findByUser(savedUser));
        Assertions.assertTrue(refreshTokenRepository.existsByUser(savedUser));
    }
}
