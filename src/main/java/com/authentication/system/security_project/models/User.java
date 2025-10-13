package com.authentication.system.security_project.models;

import com.authentication.system.security_project.enums.Gender;
import com.authentication.system.security_project.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
