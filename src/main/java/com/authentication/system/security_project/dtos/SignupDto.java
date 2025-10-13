package com.authentication.system.security_project.dtos;

import com.authentication.system.security_project.validators.GenderValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto {
    @NotBlank(message = "Name can not be blank")
    private String name;
    @Min(value = 10, message = "Only user with the age more than 10 can signup")
    @NotNull(message = "Please provide valid age")
    private int age;
    @NotNull(message = "Please provide valid Gender")
    @GenderValidator
    private String gender;
    @Email(message = "Please enter valid email")
    @NotBlank(message = "Please enter valid email")
    private String email;
    @NotBlank(message = "Please provide non empty ,valid password")
    private String password;
}
