package com.authentication.system.security_project.services;

import com.authentication.system.security_project.dtos.AdminsUpdateDto;
import com.authentication.system.security_project.dtos.AdminsUpdateResponseDto;
import com.authentication.system.security_project.dtos.SignupDto;
import com.authentication.system.security_project.dtos.SignupResponseDto;
import com.authentication.system.security_project.enums.Gender;
import com.authentication.system.security_project.enums.Role;
import com.authentication.system.security_project.exceptions.AdminDoesnotExistsException;
import com.authentication.system.security_project.exceptions.NotAnAdminException;
import com.authentication.system.security_project.exceptions.PasswordCompromisedException;
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

import java.util.Optional;

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

    @Test
    void shouldUpdateTheAdminIfTheUserWithGivenEmailIsAdmin() {
        AdminsUpdateDto updateDto = AdminsUpdateDto.builder()
                                                   .email("johndoe1234@gmail.com")
                                                   .password("@Johndoe9898").build();
        User admin = User.builder()
                         .id(1L)
                         .name("John Doe")
                         .age(20)
                         .gender(Gender.MALE)
                         .email("johndoe1234@gmail.com")
                         .password("encodedPassword")
                         .role(Role.ADMIN)
                         .build();
        User updatedAdmin = User.builder()
                                .id(1L)
                                .name("John Doe")
                                .age(20)
                                .gender(Gender.MALE)
                                .email("johndoe1234@gmail.com")
                                .password("newEncodedPassword")
                                .role(Role.ADMIN)
                                .build();
        AdminsUpdateResponseDto responseDto = AdminsUpdateResponseDto.builder()
                                                                     .name(updatedAdmin.getName())
                                                                     .age(updatedAdmin.getAge())
                                                                     .gender(updatedAdmin.getGender().name())
                                                                     .email(updatedAdmin.getEmail())
                                                                     .role(updatedAdmin.getRole()).build();


        when(userRepository.findByEmail(updateDto.getEmail())).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(updateDto.getPassword(), admin.getPassword())).thenReturn(false);
        when(passwordEncoder.encode(updateDto.getPassword())).thenReturn(updatedAdmin.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(updatedAdmin);
        when(userMapper.toAdminUpdateDto(any(User.class))).thenReturn(responseDto);


        AdminsUpdateResponseDto adminsUpdateResponseDto = userService.updateAdmin(updateDto);
        Assertions.assertNotNull(adminsUpdateResponseDto);

        Assertions.assertEquals(responseDto.getName(), adminsUpdateResponseDto.getName());
        Assertions.assertEquals(responseDto.getAge(), adminsUpdateResponseDto.getAge());
        Assertions.assertEquals(responseDto.getGender(), adminsUpdateResponseDto.getGender());
        Assertions.assertEquals(responseDto.getEmail(), adminsUpdateResponseDto.getEmail());
        Assertions.assertEquals(responseDto.getRole(), adminsUpdateResponseDto.getRole());

        verify(userRepository).findByEmail(updateDto.getEmail());
        verify(passwordEncoder).encode(updateDto.getPassword());
        verify(userRepository).save(any(User.class));
        verify(userMapper).toAdminUpdateDto(any(User.class));
    }

    @Test
    void shouldThrowAdminDoesNotExistsException() {
        AdminsUpdateDto updateDto = AdminsUpdateDto.builder()
                                                   .email("johndoe1234@gmail.com")
                                                   .password("@Johndoe9898").build();
        when(userRepository.findByEmail(updateDto.getEmail())).thenThrow(new AdminDoesnotExistsException("Admin with this email does not exists"));
        Assertions.assertThrows(AdminDoesnotExistsException.class, () -> userService.updateAdmin(updateDto));
        verify(userRepository).findByEmail(updateDto.getEmail());
    }

    @Test
    void shouldThrowPasswordCompromisedExceptionIfSamePassword() {
        AdminsUpdateDto updateDto = AdminsUpdateDto.builder()
                                                   .email("johndoe1234@gmail.com")
                                                   .password("@Johndoe9898").build();
        User admin = User.builder()
                         .id(1L)
                         .name("John Doe")
                         .age(20)
                         .gender(Gender.MALE)
                         .email("johndoe1234@gmail.com")
                         .password("encodedPassword")
                         .role(Role.ADMIN)
                         .build();
        when(userRepository.findByEmail(updateDto.getEmail())).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(updateDto.getPassword(), admin.getPassword())).thenReturn(true);

        Assertions.assertThrows(PasswordCompromisedException.class, () -> userService.updateAdmin(updateDto));

        verify(userRepository).findByEmail(updateDto.getEmail());
        verify(passwordEncoder).matches(updateDto.getPassword(), admin.getPassword());

    }

    @Test
    void shouldThrowNotAnAdminException() {
        AdminsUpdateDto updateDto = AdminsUpdateDto.builder()
                                                   .email("johndoe1234@gmail.com")
                                                   .password("@Johndoe9898").build();
        User user = User.builder()
                        .id(1L)
                        .name("John Doe")
                        .age(20)
                        .gender(Gender.MALE)
                        .email("johndoe1234@gmail.com")
                        .password("encodedPassword")
                        .role(Role.USER)
                        .build();
        when(userRepository.findByEmail(updateDto.getEmail())).thenReturn(Optional.of(user));
        Assertions.assertThrows(NotAnAdminException.class, () -> userService.updateAdmin(updateDto));

    }

    @Test
    void shouldUpdateTheManagerIfManagerExists() {
        AdminsUpdateDto updateDto = AdminsUpdateDto.builder()
                                                   .email("johndoe1234@gmail.com")
                                                   .password("@Johndoe9898").build();
        User manager = User.builder()
                           .id(1L)
                           .name("John Doe")
                           .age(20)
                           .gender(Gender.MALE)
                           .email("johndoe1234@gmail.com")
                           .password("encodedPassword")
                           .role(Role.MANAGER)
                           .build();
        User updatedManager = User.builder()
                                  .id(1L)
                                  .name("John Doe")
                                  .age(20)
                                  .gender(Gender.MALE)
                                  .email("johndoe1234@gmail.com")
                                  .password("newEncodedPassword")
                                  .role(Role.MANAGER)
                                  .build();
        AdminsUpdateResponseDto responseDto = AdminsUpdateResponseDto.builder()
                                                                     .name(updatedManager.getName())
                                                                     .age(updatedManager.getAge())
                                                                     .gender(updatedManager.getGender().name())
                                                                     .email(updatedManager.getEmail())

                                                                     .role(updatedManager.getRole()).build();
        when(userRepository.findByEmail(updateDto.getEmail())).thenReturn(Optional.of(manager));
        when(passwordEncoder.matches(updateDto.getPassword(), manager.getPassword())).thenReturn(false);
        when(passwordEncoder.encode(updateDto.getPassword())).thenReturn(updatedManager.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(updatedManager);
        when(userMapper.toAdminUpdateDto(any(User.class))).thenReturn(responseDto);

        AdminsUpdateResponseDto adminsUpdateResponseDto = userService.updateManager(updateDto);

        Assertions.assertNotNull(adminsUpdateResponseDto);
        Assertions.assertEquals(responseDto.getName(), adminsUpdateResponseDto.getName());
        Assertions.assertEquals(responseDto.getAge(), adminsUpdateResponseDto.getAge());
        Assertions.assertEquals(responseDto.getGender(), adminsUpdateResponseDto.getGender());
        Assertions.assertEquals(responseDto.getEmail(), adminsUpdateResponseDto.getEmail());
        Assertions.assertEquals(responseDto.getRole(), adminsUpdateResponseDto.getRole());

        verify(userRepository).findByEmail(updateDto.getEmail());
        verify(passwordEncoder).encode(updateDto.getPassword());
        verify(userRepository).save(any(User.class));
        verify(userMapper).toAdminUpdateDto(any(User.class));
    }

    @Test
    void shouldCreateManager() {
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
                             .role(Role.MANAGER)
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
}
