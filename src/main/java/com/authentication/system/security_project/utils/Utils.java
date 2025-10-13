package com.authentication.system.security_project.utils;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "utils")
@Data
@Getter
@Component
public class Utils {
    private Admin admin;
    private Manager manager;
    private Jwt jwt;

    @Data
    public static class Admin {
        private String name;
        private int age;
        private String gender;
        private String email;
        private String password;

    }

    @Data
    public static class Manager {
        private String email;
        private String password;
        private String name;
        private int age;
        private String gender;
    }

    @Data
    public static class Jwt {
        private String secret;
        private int expiry;

    }
}
