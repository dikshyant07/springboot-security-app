package com.authentication.system.security_project.security;

import com.authentication.system.security_project.configurations.CustomAccessDeniedHandler;
import com.authentication.system.security_project.filters.JwtFilter;
import com.authentication.system.security_project.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static com.authentication.system.security_project.enums.Permission.*;
import static com.authentication.system.security_project.enums.Role.ADMIN;
import static com.authentication.system.security_project.enums.Role.MANAGER;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final String adminRoute = "/api/v1/admin/**";
    private final String managmentRoute = "/api/v1/management/**";
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(CustomAccessDeniedHandler customAccessDeniedHandler, JwtFilter jwtFilter) {
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                   .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                   .authorizeHttpRequests(request -> request
                           .requestMatchers("/v3/api-docs/**",
                                            "/swagger-ui/**",
                                            "/swagger-ui.html",
                                            "/swagger-resources/**",
                                            "/webjars/**",
                                            "/actuator/**").permitAll()
                           .requestMatchers("/api/v1/public/**").permitAll()

                           .requestMatchers(adminRoute).hasRole(ADMIN.name())
                           .requestMatchers(GET, adminRoute).hasAnyAuthority(ADMIN_READ.getPermissionName())
                           .requestMatchers(POST, adminRoute).hasAnyAuthority(ADMIN_CREATE.getPermissionName())
                           .requestMatchers(PUT, adminRoute).hasAnyAuthority(ADMIN_UPDATE.getPermissionName())
                           .requestMatchers(DELETE, adminRoute).hasAnyAuthority(ADMIN_DELETE.getPermissionName())

                           .requestMatchers(managmentRoute).hasAnyRole(ADMIN.name(), MANAGER.name())
                           .requestMatchers(GET, managmentRoute).hasAnyAuthority(ADMIN_READ.getPermissionName(), MANAGER_READ.getPermissionName())
                           .requestMatchers(POST, managmentRoute).hasAnyAuthority(ADMIN_CREATE.getPermissionName(), MANAGER_CREATE.getPermissionName())
                           .requestMatchers(PUT, managmentRoute).hasAnyAuthority(ADMIN_UPDATE.getPermissionName(), MANAGER_UPDATE.getPermissionName())
                           .requestMatchers(DELETE, managmentRoute).hasAnyAuthority(ADMIN_DELETE.getPermissionName(), MANAGER_DELETE.getPermissionName())
                           .anyRequest().authenticated())
                   .exceptionHandling(e -> e
                           .accessDeniedHandler(customAccessDeniedHandler))
                   .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                   .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();

    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, CustomUserDetailsService customUserDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
