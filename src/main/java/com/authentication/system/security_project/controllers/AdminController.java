package com.authentication.system.security_project.controllers;

import com.authentication.system.security_project.dtos.AdminsUpdateDto;
import com.authentication.system.security_project.dtos.AdminsUpdateResponseDto;
import com.authentication.system.security_project.dtos.SignupDto;
import com.authentication.system.security_project.dtos.SignupResponseDto;
import com.authentication.system.security_project.projections.RefreshTokenWithUserProjection;
import com.authentication.system.security_project.services.RefreshTokenService;
import com.authentication.system.security_project.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AdminController(UserService userService, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping("/greet")
    public ResponseEntity<String> greetAdmin() {
        return new ResponseEntity<>("Welcome back,admin", HttpStatus.OK);
    }

    @PutMapping("/update-admin")
    public ResponseEntity<AdminsUpdateResponseDto> updateAdmin(@RequestBody @Valid AdminsUpdateDto updateDto) {
        AdminsUpdateResponseDto adminsUpdateResponseDto = userService.updateAdmin(updateDto);
        return new ResponseEntity<>(adminsUpdateResponseDto, HttpStatus.OK);
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<SignupResponseDto>> getAllUsers(
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize,
            @RequestParam(name = "sortDir", required = false, defaultValue = "ASC") String sortDir,
            @RequestParam(name = "property", required = false, defaultValue = "id") String property,
            @RequestParam(name = "search", required = false) String search

    ) {
        Sort.Direction dir = sortDir.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(dir, property));
        List<SignupResponseDto> allUsers = userService.getAllUsers(pageable, search);
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("/user-token")
    public ResponseEntity<List<RefreshTokenWithUserProjection>> getUserWithTokens() {
        List<RefreshTokenWithUserProjection> userWithTokens = refreshTokenService.getUserWithTokens();
        return new ResponseEntity<>(userWithTokens, HttpStatus.OK);
    }

    @PostMapping("/create-manager")
    public ResponseEntity<SignupResponseDto> createManager(@RequestBody @Valid SignupDto signupDto) {
        SignupResponseDto manager = userService.createManager(signupDto);
        return new ResponseEntity<>(manager, HttpStatus.CREATED);

    }
}
