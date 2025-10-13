package com.authentication.system.security_project.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthenticationServiceTest {
    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationServiceTest(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Test
    public void testService() {

    }
}
