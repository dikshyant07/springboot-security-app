package com.authentication.system.security_project.repository;

import com.authentication.system.security_project.enums.Gender;
import com.authentication.system.security_project.enums.Role;
import com.authentication.system.security_project.models.User;
import com.authentication.system.security_project.repositories.UserReposirory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
class UserRepositoryTest {
    private final UserReposirory userReposirory;

    @Autowired
    public UserRepositoryTest(UserReposirory userReposirory) {
        this.userReposirory = userReposirory;
    }

    @Test
    void testCustomQueryMethods() {
        User user = User.builder()
                        .name("John Doe")
                        .age(30)
                        .gender(Gender.MALE)
                        .email("john.doe@example.com")
                        .password("password123")
                        .role(Role.USER)
                        .build();
        User savedUser = userReposirory.save(user);
        Optional<User> existingUser = userReposirory.findByEmail("john.doe@example.com");

        Assertions.assertNotNull(savedUser);
        Assertions.assertTrue(existingUser.isPresent());
        Assertions.assertTrue(userReposirory.existsByEmail("john.doe@example.com"));


    }
}
