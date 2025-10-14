package com.authentication.system.security_project.services;

import com.authentication.system.security_project.dtos.SignupDto;
import com.authentication.system.security_project.dtos.SignupResponseDto;
import com.authentication.system.security_project.enums.Gender;
import com.authentication.system.security_project.enums.Role;
import com.authentication.system.security_project.exceptions.UserAlreadyExistsException;
import com.authentication.system.security_project.mappers.UserMapper;
import com.authentication.system.security_project.models.User;
import com.authentication.system.security_project.repositories.UserReposirory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserReposirory userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;

    @Test
    void UserService_UserCreate_ReturnsSignupResponseDto() {
        SignupDto signupDto = SignupDto.builder()
                                       .name("John Doe")
                                       .age(25)
                                       .gender("MALE")
                                       .email("john.doe@example.com")
                                       .password("StrongP@ssword123")
                                       .build();
        User mappedUser = User.builder()
                              .name(signupDto.getName())
                              .age(signupDto.getAge())
                              .gender(Gender.valueOf(signupDto.getGender()))
                              .email(signupDto.getEmail())
                              .password(signupDto.getPassword())
                              .build();

        User savedUser = User.builder()
                             .id(1L)
                             .name(signupDto.getName())
                             .age(signupDto.getAge())
                             .gender(Gender.valueOf(signupDto.getGender()))
                             .email(signupDto.getEmail())
                             .password("encodedPassword")
                             .role(Role.USER)
                             .build();

        SignupResponseDto responseDto = SignupResponseDto.builder()
                                                         .userId(1L)
                                                         .name(savedUser.getName())
                                                         .age(savedUser.getAge())
                                                         .gender(savedUser.getGender().name())
                                                         .role(savedUser.getRole())
                                                         .email(savedUser.getEmail())
                                                         .build();

        when(userRepository.existsByEmail(signupDto.getEmail())).thenReturn(false);
        when(userMapper.toUser(signupDto)).thenReturn(mappedUser);
        when(passwordEncoder.encode(signupDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(responseDto);

        SignupResponseDto signupResponseDto = userService.createAccount(signupDto);

        Assertions.assertNotNull(signupResponseDto);
        Assertions.assertEquals(savedUser.getId(), signupResponseDto.getUserId());
        Assertions.assertEquals(savedUser.getName(), signupResponseDto.getName());
        Assertions.assertEquals(savedUser.getEmail(), signupResponseDto.getEmail());
        Assertions.assertEquals(savedUser.getRole(), signupResponseDto.getRole());
        Assertions.assertEquals(savedUser.getGender().name(), signupResponseDto.getGender());

        verify(userRepository).existsByEmail(signupDto.getEmail());
        verify(userMapper).toUser(signupDto);
        verify(passwordEncoder).encode(signupDto.getPassword());
        verify(userRepository).save(any(User.class));

    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionIfExists() {
        SignupDto signupDto = SignupDto.builder()
                                       .name("John Doe")
                                       .age(25)
                                       .gender("MALE")
                                       .email("john.doe@example.com")
                                       .password("StrongP@ssword123")
                                       .build();
        when(userRepository.existsByEmail(signupDto.getEmail())).thenReturn(true);
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.createAccount(signupDto));

        verify(userRepository).existsByEmail(signupDto.getEmail());

    }

}
