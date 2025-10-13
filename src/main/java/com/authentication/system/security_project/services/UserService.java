package com.authentication.system.security_project.services;

import com.authentication.system.security_project.dtos.AdminsUpdateDto;
import com.authentication.system.security_project.dtos.AdminsUpdateResponseDto;
import com.authentication.system.security_project.dtos.SignupDto;
import com.authentication.system.security_project.dtos.SignupResponseDto;
import com.authentication.system.security_project.exceptions.*;
import com.authentication.system.security_project.mappers.UserMapper;
import com.authentication.system.security_project.models.User;
import com.authentication.system.security_project.repositories.UserReposirory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.authentication.system.security_project.enums.Role.*;

@Service
public class UserService {
    private UserReposirory userReposirory;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    @Autowired
    public UserService(UserReposirory userReposirory, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userReposirory = userReposirory;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public SignupResponseDto createAccount(SignupDto signupDto) {
        if (userReposirory.existsByEmail(signupDto.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
        User user = userMapper.toUser(signupDto);
        String encodedPassword = passwordEncoder.encode(signupDto.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(USER);
        User savedUser = userReposirory.save(user);
        return userMapper.toDto(savedUser);
    }

    public AdminsUpdateResponseDto updateAdmin(AdminsUpdateDto adminsUpdateDto) {
        User admin = userReposirory.findByEmail(adminsUpdateDto.getEmail()).orElseThrow(() -> new AdminDoesnotExistsException("User with this email does not exists"));
        if (!admin.getRole().equals(ADMIN)) {
            throw new NotAnAdminException("The user with this email is not an admin ,so failed to update");
        }
        if (passwordEncoder.matches(adminsUpdateDto.getPassword(), admin.getPassword())) {
            throw new PasswordCompromisedException("Your password can not be same as previous one");
        }
        String encodedPassword = passwordEncoder.encode(adminsUpdateDto.getPassword());
        admin.setPassword(encodedPassword);
        User updatedAdmin = userReposirory.save(admin);
        return userMapper.toAdminUpdateDto(updatedAdmin);
    }

    public AdminsUpdateResponseDto updateManager(AdminsUpdateDto adminsUpdateDto) {
        User manager = userReposirory.findByEmail(adminsUpdateDto.getEmail()).orElseThrow(() -> new AdminDoesnotExistsException("User with this email does not exists"));
        if (!manager.getRole().equals(MANAGER)) {
            throw new NotTheManagerException("The user with this email is not an manager ,so failed to update");
        }
        if (passwordEncoder.matches(adminsUpdateDto.getPassword(), manager.getPassword())) {
            throw new PasswordCompromisedException("Your password can not be same as previous one");
        }
        String encodedPassword = passwordEncoder.encode(adminsUpdateDto.getPassword());
        manager.setPassword(encodedPassword);
        User updatedManager = userReposirory.save(manager);
        return userMapper.toAdminUpdateDto(updatedManager);
    }

    public List<SignupResponseDto> getAllUsers(Pageable pageable, String search) {
        if (search == null) {
            return userMapper.toList(userReposirory.findAll(pageable).getContent());
        }
        return List.of(userMapper.toDto(userReposirory.findByEmail(search).orElse(null)));
    }

    public SignupResponseDto createManager(SignupDto signupDto) {
        if (userReposirory.existsByEmail(signupDto.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
        User manager = userMapper.toUser(signupDto);
        String encodedPassword = passwordEncoder.encode(signupDto.getPassword());
        manager.setRole(MANAGER);
        manager.setPassword(encodedPassword);
        return userMapper.toDto(userReposirory.save(manager));
    }
}
