package com.authentication.system.security_project.services;

import com.authentication.system.security_project.enums.Gender;
import com.authentication.system.security_project.models.User;
import com.authentication.system.security_project.repositories.UserReposirory;
import com.authentication.system.security_project.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.authentication.system.security_project.enums.Role.ADMIN;
import static com.authentication.system.security_project.enums.Role.MANAGER;

@Service
public class AdminsCreationService implements CommandLineRunner {
    private PasswordEncoder passwordEncoder;
    private Utils utils;
    private UserReposirory userReposirory;

    @Autowired
    public AdminsCreationService(PasswordEncoder passwordEncoder, Utils utils, UserReposirory userReposirory) {
        this.passwordEncoder = passwordEncoder;
        this.utils = utils;
        this.userReposirory = userReposirory;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userReposirory.existsByEmail(utils.getAdmin().getEmail())) {
            String encodedPassword = passwordEncoder.encode(utils.getAdmin().getPassword());
            User admin = User.builder()
                             .name(utils.getAdmin().getName())
                             .age(utils.getAdmin().getAge())
                             .gender(Gender.valueOf(utils.getAdmin().getGender()))
                             .email(utils.getAdmin().getEmail())
                             .password(encodedPassword)
                             .role(ADMIN)
                             .build();
            userReposirory.save(admin);

        }
        if (!userReposirory.existsByEmail(utils.getManager().getEmail())) {
            String encodedPassword = passwordEncoder.encode(utils.getManager().getPassword());
            User manager = User.builder()
                               .name(utils.getManager().getName())
                               .age(utils.getManager().getAge())
                               .gender(Gender.valueOf(utils.getManager().getGender()))
                               .email(utils.getManager().getEmail())
                               .password(encodedPassword)
                               .role(MANAGER)

                               .build();
            userReposirory.save(manager);
        }
    }
}
