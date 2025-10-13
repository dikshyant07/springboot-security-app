package com.authentication.system.security_project.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenDto {
    @NotBlank(message = "Please provide non empty ,valid token")
    private String token;

}
