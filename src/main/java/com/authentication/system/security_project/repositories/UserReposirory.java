package com.authentication.system.security_project.repositories;

import com.authentication.system.security_project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserReposirory extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String name);

    boolean existsByEmail(String email);
}
