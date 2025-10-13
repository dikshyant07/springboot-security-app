package com.authentication.system.security_project.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogoutResponseDto {
    private String message;
    private boolean success;
    private HttpStatus httpStatus;
}
