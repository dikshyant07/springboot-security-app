package com.authentication.system.security_project.projections;

import com.authentication.system.security_project.enums.Gender;
import com.authentication.system.security_project.enums.Role;


public interface RefreshTokenWithUserProjection {
    String getName();

    Integer getAge();

    Gender getGender();

    String getEmail();

    Role getRole();

    String getToken();
}
