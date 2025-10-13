package com.authentication.system.security_project.dtos;

import com.authentication.system.security_project.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminsUpdateResponseDto {
    private String name;
    private int age;
    private String gender;
    private Role role;
    private String email;
}
