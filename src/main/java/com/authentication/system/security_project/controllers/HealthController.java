package com.authentication.system.security_project.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {
    @GetMapping("/check")
    public ResponseEntity<String> checkHealth() {
        String response = "Tomcat is running in port 8090";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
