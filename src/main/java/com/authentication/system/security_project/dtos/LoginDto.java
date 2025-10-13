package com.authentication.system.security_project.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {
    @Email(message = "Please enter valid email")
    @NotBlank(message = "Please enter valid email")
    private String email;
    @NotBlank(message = "Please provide non empty ,valid password")
    private String password;
}
