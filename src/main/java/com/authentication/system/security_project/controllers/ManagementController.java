package com.authentication.system.security_project.controllers;

import com.authentication.system.security_project.dtos.AdminsUpdateDto;
import com.authentication.system.security_project.dtos.AdminsUpdateResponseDto;
import com.authentication.system.security_project.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/management")
public class ManagementController {
    private final UserService userService;

    @Autowired
    public ManagementController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/update-manager")
    public ResponseEntity<AdminsUpdateResponseDto> updateManager(@RequestBody @Valid AdminsUpdateDto updateDto) {
        AdminsUpdateResponseDto adminsUpdateResponseDto = userService.updateManager(updateDto);
        return new ResponseEntity<>(adminsUpdateResponseDto, HttpStatus.OK);
    }
}
