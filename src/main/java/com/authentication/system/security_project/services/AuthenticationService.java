package com.authentication.system.security_project.services;

import com.authentication.system.security_project.dtos.LoginDto;
import com.authentication.system.security_project.dtos.TokensResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private RefreshTokenService refreshTokenService;

    @Autowired
    public AuthenticationService(JwtService jwtService, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    public TokensResponseDto login(LoginDto loginDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            String accessToken = jwtService.generateToken(loginDto.getEmail());
            String refreshToken = refreshTokenService.generateRefreshToken(loginDto.getEmail()).getToken();
            return TokensResponseDto.builder()
                                    .accessToken(accessToken).refreshToken(refreshToken).build();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Please provide valid credentials to login");
        }
    }
}
