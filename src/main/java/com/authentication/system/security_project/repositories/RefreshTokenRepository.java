package com.authentication.system.security_project.repositories;

import com.authentication.system.security_project.models.RefreshToken;
import com.authentication.system.security_project.models.User;
import com.authentication.system.security_project.projections.RefreshTokenWithUserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsByToken(String name);

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    boolean existsByUser(User user);

    @Query("SELECT u.name as name,u.age as age,u.gender as gender,u.email as email,u.role as role,r.token as token FROM RefreshToken r OUTER JOIN User u ON u.id=r.user.id")
    public List<RefreshTokenWithUserProjection> getTokenWithUser();
}
