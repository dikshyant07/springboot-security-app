package com.authentication.system.security_project.services;

import com.authentication.system.security_project.models.User;
import com.authentication.system.security_project.repositories.UserReposirory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserReposirory userReposirory;

    @Autowired
    public CustomUserDetailsService(UserReposirory userReposirory) {
        this.userReposirory = userReposirory;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userReposirory.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User with this username does not exists"));
        return new UserPrincipal(user);

    }
}
