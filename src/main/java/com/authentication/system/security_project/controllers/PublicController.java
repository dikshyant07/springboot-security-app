package com.authentication.system.security_project.controllers;

import com.authentication.system.security_project.dtos.*;
import com.authentication.system.security_project.services.AuthenticationService;
import com.authentication.system.security_project.services.RefreshTokenService;
import com.authentication.system.security_project.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {
    private UserService userService;
    private AuthenticationService authenticationService;
    private RefreshTokenService refreshTokenService;

    @Autowired
    public PublicController(UserService userService, AuthenticationService authenticationService, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> createAccount(@RequestBody @Valid SignupDto signupDto) {
        SignupResponseDto responseDto = userService.createAccount(signupDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<TokensResponseDto> login(LoginDto loginDto) {
        TokensResponseDto tokens = authenticationService.login(loginDto);
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokensResponseDto> refreshToken(@RequestBody @Valid RefreshTokenDto refreshTokenDto) {
        TokensResponseDto tokensResponseDto = refreshTokenService.refreshTheToken(refreshTokenDto);
        return new ResponseEntity<>(tokensResponseDto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDto> logout(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        refreshTokenService.logout(refreshTokenDto);
        LogoutResponseDto logoutResponse = LogoutResponseDto.builder().httpStatus(HttpStatus.OK)
                                                            .message("Successfully logged out the user")
                                                            .success(true)
                                                            .build();
        return new ResponseEntity<>(logoutResponse, HttpStatus.OK);
    }


}
